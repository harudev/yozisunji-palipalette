package com.yozisunji.palipalette;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;


public class SVGParser
{
	public static ArrayList<PaliLayer> Layers;
	public static int Layersize = 1;
	SVGParser()
	{
		Layers = new ArrayList<PaliLayer>();
	}
	
	public void addLayer()
	{
		Layers.add(new PaliLayer());
		PaliCanvas.currentLayer = Layers.size()-1;
		PaliCanvas.currentObject = -1;
	}
	
	public void deleteLayer(int index)
	{
		Layers.remove(index);
		PaliCanvas.currentLayer = index - 1;
		PaliCanvas.currentObject = 0;
	}
	
	public void upLayer(int index)
	{
		PaliLayer temp = Layers.get(index);
		Layers.set(index, Layers.get(index-1));
		Layers.set(index-1, temp);
	}
	public void downLayer(int index)
	{
		PaliLayer temp = Layers.get(index);
		Layers.set(index, Layers.get(index+1));
		Layers.set(index+1, temp);
	}
	
	public void parse(InputStream in) {
//      Util.debug("Parsing SVG...");
     
    	try
    	{    	  
          long start = System.currentTimeMillis();
          SAXParserFactory spf = SAXParserFactory.newInstance();
          SAXParser sp = spf.newSAXParser();
          XMLReader xr = sp.getXMLReader();
          SVGHandler handler = new SVGHandler();
          Layers.clear();
          xr.setContentHandler(handler);
          xr.parse(new InputSource(in));
    	}
    	catch(SAXException e)
    	{
    		
    	} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}




class SVGHandler extends DefaultHandler {
	private String elementValue = null;
    private Boolean elementOn = false;
    // Scratch rect (so we aren't constantly making new ones)
    RectF rect = new RectF();
    public Rect bounds = null;
    RectF limits = new RectF(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

	
    
    Integer searchColor = null;
    Integer replaceColor = null;

    boolean whiteMode = false;

    boolean pushed = false;
  
    SVGHandler()
    {
    	
    }

    
    
    private static String getStringAttr(String name, Attributes attributes) {
        int n = attributes.getLength();
        for (int i = 0; i < n; i++) {
            if (attributes.getLocalName(i).equals(name)) {
                return attributes.getValue(i);
            }
        }
        return null;
    }

    private static Float getFloatAttr(String name, Attributes attributes) {
        return getFloatAttr(name, attributes, null);
    }

    private static Float getFloatAttr(String name, Attributes attributes, Float defaultValue) {
        String v = getStringAttr(name, attributes);
        if (v == null) {
            return defaultValue;
        } else {
            if (v.endsWith("px")) {
                v = v.substring(0, v.length() - 2);
            }
//            Log.d(TAG, "Float parsing '" + name + "=" + v + "'");
            return Float.parseFloat(v);
        }
    }

    private static Integer getHexAttr(String name, Attributes attributes) {
        String v = getStringAttr(name, attributes);
        //Util.debug("Hex parsing '" + name + "=" + v + "'");
        if (v == null) {
            return null;
        } else {
            try {
                return Integer.parseInt(v.substring(1), 16);
            } catch (NumberFormatException nfe) {
                // todo - parse word-based color here
                return null;
            }
        }
    }
    
    private static Path doPath(String s) {
        int n = s.length();
        ParserHelper ph = new ParserHelper(s, 0);
        ph.skipWhitespace();
        Path p = new Path();
        float lastX = 0;
        float lastY = 0;
        float lastX1 = 0;
        float lastY1 = 0;
        float subPathStartX = 0;
        float subPathStartY = 0;
        char prevCmd = 0;
        while (ph.pos < n) {
            char cmd = s.charAt(ph.pos);
            switch (cmd) {
                case '-':
                case '+':
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    if (prevCmd == 'm' || prevCmd == 'M') {
                        cmd = (char) (((int) prevCmd) - 1);
                        break;
                    } else if (prevCmd == 'c' || prevCmd == 'C') {
                        cmd = prevCmd;
                        break;
                    } else if (prevCmd == 'l' || prevCmd == 'L') {
                        cmd = prevCmd;
                        break;
                    }
                default: {
                    ph.advance();
                    prevCmd = cmd;
                }
            }

            boolean wasCurve = false;
            switch (cmd) {
                case 'M':
                case 'm': {
                    float x = ph.nextFloat();
                    float y = ph.nextFloat();
                    if (cmd == 'm') {
                        subPathStartX += x;
                        subPathStartY += y;
                        p.rMoveTo(x, y);
                        lastX += x;
                        lastY += y;
                    } else {
                        subPathStartX = x;
                        subPathStartY = y;
                        p.moveTo(x, y);
                        lastX = x;
                        lastY = y;
                    }
                    break;
                }
                case 'Z':
                case 'z': {
                    p.close();
                    p.moveTo(subPathStartX, subPathStartY);
                    lastX = subPathStartX;
                    lastY = subPathStartY;
                    lastX1 = subPathStartX;
                    lastY1 = subPathStartY;
                    wasCurve = true;
                    break;
                }
                case 'L':
                case 'l': {
                    float x = ph.nextFloat();
                    float y = ph.nextFloat();
                    if (cmd == 'l') {
                        p.rLineTo(x, y);
                        lastX += x;
                        lastY += y;
                    } else {
                        p.lineTo(x, y);
                        lastX = x;
                        lastY = y;
                    }
                    break;
                }
                case 'H':
                case 'h': {
                    float x = ph.nextFloat();
                    if (cmd == 'h') {
                        p.rLineTo(x, 0);
                        lastX += x;
                    } else {
                        p.lineTo(x, lastY);
                        lastX = x;
                    }
                    break;
                }
                case 'V':
                case 'v': {
                    float y = ph.nextFloat();
                    if (cmd == 'v') {
                        p.rLineTo(0, y);
                        lastY += y;
                    } else {
                        p.lineTo(lastX, y);
                        lastY = y;
                    }
                    break;
                }
                case 'C':
                case 'c': {
                    wasCurve = true;
                    float x1 = ph.nextFloat();
                    float y1 = ph.nextFloat();
                    float x2 = ph.nextFloat();
                    float y2 = ph.nextFloat();
                    float x = ph.nextFloat();
                    float y = ph.nextFloat();
                    if (cmd == 'c') {
                        x1 += lastX;
                        x2 += lastX;
                        x += lastX;
                        y1 += lastY;
                        y2 += lastY;
                        y += lastY;
                    }
                    p.cubicTo(x1, y1, x2, y2, x, y);
                    lastX1 = x2;
                    lastY1 = y2;
                    lastX = x;
                    lastY = y;
                    break;
                }
                case 'S':
                case 's': {
                    wasCurve = true;
                    float x2 = ph.nextFloat();
                    float y2 = ph.nextFloat();
                    float x = ph.nextFloat();
                    float y = ph.nextFloat();
                    if (cmd == 's') {
                        x2 += lastX;
                        x += lastX;
                        y2 += lastY;
                        y += lastY;
                    }
                    float x1 = 2 * lastX - lastX1;
                    float y1 = 2 * lastY - lastY1;
                    p.cubicTo(x1, y1, x2, y2, x, y);
                    lastX1 = x2;
                    lastY1 = y2;
                    lastX = x;
                    lastY = y;
                    break;
                }
                case 'A':
                case 'a': {
                    float rx = ph.nextFloat();
                    float ry = ph.nextFloat();
                    float theta = ph.nextFloat();
                    int largeArc = (int) ph.nextFloat();
                    int sweepArc = (int) ph.nextFloat();
                    float x = ph.nextFloat();
                    float y = ph.nextFloat();
                    drawArc(p, lastX, lastY, x, y, rx, ry, theta, largeArc, sweepArc);
                    lastX = x;
                    lastY = y;
                    break;
                }
            }
            if (!wasCurve) {
                lastX1 = lastX;
                lastY1 = lastY;
            }
            ph.skipWhitespace();
        }
        return p;
    }
    
    private static void drawArc(Path p, float lastX, float lastY, float x, float y, float rx, float ry, float theta, int largeArc, int sweepArc) {
        // todo - not implemented yet, may be very hard to do using Android drawing facilities.
    }
    
    public void startElement(String uri, String localName, String qName,
            Attributes atts) throws SAXException {
        elementOn = true; 
        Log.w("parse", localName);
        if (localName.equals("svg")) {
            int width = (int) Math.ceil(getFloatAttr("width", atts));
            int height = (int) Math.ceil(getFloatAttr("height", atts));
            SVGParser.Layers.add(new PaliLayer());
            PaliCanvas.currentObject = -1;
        } else if (localName.equals("g") && SVGParser.Layers.size()>1) {
        	SVGParser.Layers.add(new PaliLayer());
        	SVGParser.Layersize+=1;
        	PaliCanvas.currentObject = -1;
        } else if (localName.equals("circle")) {
            Float centerX = getFloatAttr("cx", atts);
            Float centerY = getFloatAttr("cy", atts);
            Float radius = getFloatAttr("r", atts);
            if (centerX != null && centerY != null && radius != null) {
            	SVGParser.Layers.get(PaliCanvas.currentLayer).objs.add(new PaliCircle("",centerX,centerY,radius));
            	PaliCanvas.currentObject++;
            }
        } else if (localName.equals("ellipse")) {
        	Float centerX = getFloatAttr("cx", atts);
            Float centerY = getFloatAttr("cy", atts);
            Float radiusX = getFloatAttr("rx", atts);
            Float radiusY = getFloatAttr("ry", atts);
            if (centerX != null && centerY != null && radiusX != null && radiusY != null) {
            	SVGParser.Layers.get(PaliCanvas.currentLayer).objs.add(new PaliEllipse("",centerX-radiusX,centerY-radiusY,centerX+radiusX,centerY+radiusY));
            	PaliCanvas.currentObject++;
            }
        } else if (localName.equals("rect")) {
        	Float centerX = getFloatAttr("x", atts);
        	Float centerY = getFloatAttr("y", atts);
        	Float width = getFloatAttr("width", atts);
        	Float height = getFloatAttr("height", atts);
        	if (centerX != null && centerY != null && width!= null && height != null) {
        		SVGParser.Layers.get(PaliCanvas.currentLayer).objs.add(new PaliRectangle("",centerX,centerY,centerX + width,centerY + height));
        		PaliCanvas.currentObject++;
        	}
        } else if (localName.equals("path")) {
        	Path path = doPath(getStringAttr("d", atts));
        	if (path != null) {
        		SVGParser.Layers.get(SVGParser.Layers.size()-1).objs.add(new PaliPencil("",path));
        		PaliCanvas.currentObject++;
        	}
        }
    }   
  
    /**
     * This will be called when the tags of the XML end.
     **/
    public void endElement(String namespaceURI, String localName, String qName)
            throws SAXException {
        if (localName.equals("svg")) {
        } 
    }

}
