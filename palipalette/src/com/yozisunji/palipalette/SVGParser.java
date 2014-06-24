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

import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;


public class SVGParser
{
	static ArrayList<PaliLayer> Layers;
	static int Layersize = 1;
	SVGParser()
	{
		Layers = new ArrayList<PaliLayer>();
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
    
     
    public void startElement(String uri, String localName, String qName,
            Attributes atts) throws SAXException {
        elementOn = true; 
        Log.w("parse", localName);
        if (localName.equals("svg")) {
            int width = (int) Math.ceil(getFloatAttr("width", atts));
            int height = (int) Math.ceil(getFloatAttr("height", atts));
            SVGParser.Layers.add(new PaliLayer());
        } else if (localName.equals("g") && SVGParser.Layers.size()>1) {
        	SVGParser.Layers.add(new PaliLayer());
        	SVGParser.Layersize+=1;
        } else if (localName.equals("circle")) {
            Float centerX = getFloatAttr("cx", atts);
            Float centerY = getFloatAttr("cy", atts);
            Float radius = getFloatAttr("r", atts);
            if (centerX != null && centerY != null && radius != null) {
            	SVGParser.Layers.get(SVGParser.Layers.size()-1).objs.add(new PaliCircle(localName,centerX,centerY,radius));
            }
        } else if (localName.equals("rect")) {
        	Float centerX = getFloatAttr("x", atts);
        	Float centerY = getFloatAttr("y", atts);
        	Float width = getFloatAttr("width", atts);
        	Float height = getFloatAttr("height", atts);
        	if (centerX != null && centerY != null && width!= null && height != null) {
        		SVGParser.Layers.get(SVGParser.Layers.size()-1).objs.add(new PaliRectangle(localName,centerX,centerY,width,height));
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
