// $Id: URIInputModule.java,v 1.3 2009-08-28 16:11:41 jean-gui Exp $
// Author: Damien LEROY.
// (c) COPYRIGHT MIT, ERCIM ant Keio, 2006.
// Please first read the full copyright statement in file COPYRIGHT.html
package org.w3c.unicorn.input;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Date;

import javax.activation.MimeType;

import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.unicorn.contract.EnumInputMethod;
import org.w3c.unicorn.util.Property;

/**
 * Class used to deal with the URI method for inputs
 * 
 * @author Damien LEROY
 */
public class URIInputModule implements InputModule {

	/**
	 * Sets the method of input to URI
	 */
	private final EnumInputMethod aEnumInputMethod = EnumInputMethod.URI;

	/**
	 * Mime-type of the input
	 */
	private MimeType aMimeType = null;

	/**
	 * URI of the input
	 */
	private String sURI = null;

	/**
	 * File found at the URI
	 */
	private File aFile = null;

	/**
	 * Constructor of the URI input module
	 * 
	 * @param aMimeType
	 *            mime-type of the input
	 * @param oInputParameterValue
	 *            parameter of the input
	 */
	protected URIInputModule(final MimeType aMimeType,
			final Object oInputParameterValue) {
		InputModule.logger.trace("Constructor.");
		if (InputModule.logger.isDebugEnabled()) {
			InputModule.logger.debug("oInputParameterValue : "
					+ oInputParameterValue + ".");
		}
		if (!(oInputParameterValue instanceof String)) {
			throw new IllegalArgumentException("Object oInputParameterValue : "
					+ oInputParameterValue.toString() + ".");
		}
		this.aMimeType = aMimeType;
		this.sURI = (String) oInputParameterValue;
		this.sURI = StringEscapeUtils.escapeHtml(this.sURI);
	}

	/**
	 * Copy an input module into a URI method module
	 * 
	 * @param aInputModule
	 *            input to copy
	 * @throws IOException
	 *             odd error occurs
	 */
	protected URIInputModule(final InputModule aInputModule) throws IOException {
		InputModule.logger.trace("Constructor.");
		if (InputModule.logger.isDebugEnabled()) {
			InputModule.logger.debug("InputModule : " + aInputModule + ".");
		}
		this.aMimeType = aInputModule.getMimeType();
		final Date aDate = new Date();
		final String sFileName;
		sFileName = "tmp_"
				+ aDate.getTime()
				+ "_."
				+ Property.getProps("extensions.properties").getProperty(this.aMimeType
						.toString());
		this.aFile = new File(Property.get("PATH_TO_TEMPORARY_FILES")
				+ sFileName);
		this.aFile.createNewFile();
		final PrintWriter aPrintWriter = new PrintWriter(aFile);
		aPrintWriter.print(aInputModule.getStringContent());
		aPrintWriter.close();
		this.sURI = Property.get("URL_TO_TEMPORARY_FILES") + sFileName;
	}

	public EnumInputMethod getEnumInputMethod() {
		InputModule.logger.trace("getEnumInputMethod");
		return this.aEnumInputMethod;
	}

	public MimeType getMimeType() {
		InputModule.logger.trace("getMimeType");
		return this.aMimeType;
	}

	public Object getParameterValue() {
		InputModule.logger.trace("getParameterValue");
		return this.sURI;
	}

	public String getStringContent() throws IOException {
		InputModule.logger.trace("getString.");
		final URL aURL = new URL(this.sURI);
		final String sResult = (String) aURL.openConnection().getContent();
		if (InputModule.logger.isDebugEnabled()) {
			InputModule.logger.debug("sResult : " + sResult + ".");
		}
		return sResult;
	}

	public String getURI() {
		InputModule.logger.trace("getURI");
		return this.sURI;
	}

	/**
	 * Dispose the object
	 */
	public void dispose() {
		InputModule.logger.trace("dispose");
		if (null != this.aFile && this.aFile.delete()) {
			InputModule.logger.info("File deleted.");
			this.aFile = null;
		}
	}

	/**
	 * Prints the object
	 */
	@Override
	public String toString() {
		final int iStringBufferSize = 500;
		final StringBuffer aStringBuffer = new StringBuffer(iStringBufferSize);
		aStringBuffer.append("URIInputModule{");
		aStringBuffer.append("mimetype: ").append(this.aMimeType);
		aStringBuffer.append(", uri: ").append(this.sURI);
		aStringBuffer.append("}").append(this.sURI);

		return aStringBuffer.toString();
	}

}
