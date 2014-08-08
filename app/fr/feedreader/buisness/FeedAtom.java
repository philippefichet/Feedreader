package fr.feedreader.buisness;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import fr.feedreader.models.FeedItem;

public class FeedAtom extends FeedAbstract {

//    private static final String CONTENT_TYPE_HTML = "html";
//    private static final String CONTENT_TYPE_XHTML = "xhtml";
//    private static final String CONTENT_TYPE_TEXT = "text";
//    
//    private static final String TAG_ENTRY = "entry";
//    private static final String TAG_LINK = "link";
//    private static final String TAG_CONTENT = "content";
//    
//    private static final String ATTRIBUTE_REL = "rel";
//    private static final String ATTRIBUTE_HREF = "href";

    
    /**
     * Receive notification of the start of an element.
     *
     * <p>By default, do nothing.  Application writers may override this
     * method in a subclass to take specific actions at the start of
     * each element (such as allocating a new tree node or writing
     * output to a file).</p>
     *
     * @param uri The Namespace URI, or the empty string if the
     *        element has no Namespace URI or if Namespace
     *        processing is not being performed.
     * @param localName The local name (without prefix), or the
     *        empty string if Namespace processing is not being
     *        performed.
     * @param qName The qualified name (with prefix), or the
     *        empty string if qualified names are not available.
     * @param attributes The attributes attached to the element.  If
     *        there are no attributes, it shall be an empty
     *        Attributes object.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see org.xml.sax.ContentHandler#startElement
     */
	@Override
    public void startElement (String uri, String localName,
                              String qName, Attributes attributes)
        throws SAXException
    {
//    	System.out.println("startElement: " + qName);
    	currentData = null;
        if (qName.equalsIgnoreCase("entry")) {
        	currentFeedItem = new FeedItem();
        } else if(qName.equalsIgnoreCase("link") && currentFeedItem != null) {
        	String rel = attributes.getValue("rel");
        	String href = attributes.getValue("href");
        	if (rel == null || rel.equals("alternate")) {
            	currentFeedItem.setLink(href);
        	} else if(rel.equals("enclosure")) {
        		currentFeedItem.setEnclosure(href);
        	}
        }
    }


    /**
     * Receive notification of the end of an element.
     *
     * <p>By default, do nothing.  Application writers may override this
     * method in a subclass to take specific actions at the end of
     * each element (such as finalising a tree node or writing
     * output to a file).</p>
     *
     * @param uri The Namespace URI, or the empty string if the
     *        element has no Namespace URI or if Namespace
     *        processing is not being performed.
     * @param localName The local name (without prefix), or the
     *        empty string if Namespace processing is not being
     *        performed.
     * @param qName The qualified name (with prefix), or the
     *        empty string if qualified names are not available.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see org.xml.sax.ContentHandler#endElement
     */
	@Override
    public void endElement (String uri, String localName, String qName)
        throws SAXException
    {
        if (qName.equalsIgnoreCase("entry")) {
        	feedItems.add(currentFeedItem);
        	currentFeedItem = null;
        } else if(qName.equalsIgnoreCase("id") && currentFeedItem != null) {
        	currentFeedItem.setFeedItemId(currentData);
	    } else if(qName.equalsIgnoreCase("title") && currentFeedItem != null) {
        	currentFeedItem.setTitle(currentData);
	    } else if(qName.equalsIgnoreCase("link") && currentFeedItem != null) {
//	    	System.out.println("*******************************************");
//        	currentFeedItem.setLink(currentData);
	    } else if(qName.equalsIgnoreCase("summary") && currentFeedItem != null) {
        	currentFeedItem.setSummary(currentData);
	    } else if(qName.equalsIgnoreCase("updated") && currentFeedItem != null) {
	    	try {
	    		currentFeedItem.setUpdated(sdf.parse(currentData));
	    	} catch (ParseException parseException) {
	    		LoggerFactory.getLogger(getClass()).warn("Impossible de parser la date \"" + currentData + "\"");
	    	}
	    } else if(localName.equals("content")) {
//            currentFeedItem.setContent(currentData);
//	    	System.out.println(" - " + qName);
	    } else if(currentFeedItem != null) {
//	    	System.out.println(" - " + qName);
	    } 
    }
    
    public String getType() {
    	return "atom";
    }
}
