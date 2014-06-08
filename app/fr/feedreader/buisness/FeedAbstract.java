package fr.feedreader.buisness;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import fr.feedreader.models.FeedItem;

public abstract class FeedAbstract {

    protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
    protected List<FeedItem> feedItems = new ArrayList<FeedItem>();
    protected FeedItem currentFeedItem = null;
    protected String currentData = null;

    /**
     * Receive notification of the start of an element.
     *
     * <p>
     * By default, do nothing. Application writers may override this method in a
     * subclass to take specific actions at the start of each element (such as
     * allocating a new tree node or writing output to a file).</p>
     *
     * @param uri The Namespace URI, or the empty string if the element has no
     * Namespace URI or if Namespace processing is not being performed.
     * @param localName The local name (without prefix), or the empty string if
     * Namespace processing is not being performed.
     * @param qName The qualified name (with prefix), or the empty string if
     * qualified names are not available.
     * @param attributes The attributes attached to the element. If there are no
     * attributes, it shall be an empty Attributes object.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly wrapping
     * another exception.
     * @see org.xml.sax.ContentHandler#startElement
     */
    public abstract void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException;

    /**
     * Receive notification of the end of an element.
     *
     * <p>
     * By default, do nothing. Application writers may override this method in a
     * subclass to take specific actions at the end of each element (such as
     * finalising a tree node or writing output to a file).</p>
     *
     * @param uri The Namespace URI, or the empty string if the element has no
     * Namespace URI or if Namespace processing is not being performed.
     * @param localName The local name (without prefix), or the empty string if
     * Namespace processing is not being performed.
     * @param qName The qualified name (with prefix), or the empty string if
     * qualified names are not available.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly wrapping
     * another exception.
     * @see org.xml.sax.ContentHandler#endElement
     */
    public abstract void endElement(String uri, String localName, String qName) throws SAXException;

    /**
     * Receive notification of character data inside an element.
     *
     * <p>
     * By default, do nothing. Application writers may override this method to
     * take specific actions for each chunk of character data (such as adding
     * the data to a node or buffer, or printing it to a file).</p>
     *
     * @param ch The characters.
     * @param start The start position in the character array.
     * @param length The number of characters to use from the character array.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly wrapping
     * another exception.
     * @see org.xml.sax.ContentHandler#characters
     */
    public void characters(char ch[], int start, int length)
            throws SAXException {
        if (currentData == null) {
            currentData = new String(ch, start, length);
        } else {
            currentData += new String(ch, start, length);
        }
    }

    public List<FeedItem> getFeedItems() {
        return feedItems;
    }

    public abstract String getType();
}
