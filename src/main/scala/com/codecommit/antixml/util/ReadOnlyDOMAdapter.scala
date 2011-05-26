/*
 * Copyright (c) 2011, Daniel Spiewak
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer. 
 * - Redistributions in binary form must reproduce the above copyright notice, this
 *   list of conditions and the following disclaimer in the documentation and/or
 *   other materials provided with the distribution.
 * - Neither the name of "Anti-XML" nor the names of its contributors may
 *   be used to endorse or promote products derived from this software without
 *   specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.codecommit.antixml
package util

import org.w3c.dom.{ Node => DomNode }
import org.w3c.dom.{ NodeList => DomNodeList }
import org.w3c.dom.{ Document => DomDocument }
import org.w3c.dom.{ Attr => DomAttr }
import org.w3c.dom.{ CharacterData=> DomCharacterData}
import org.w3c.dom.{ Text => DomText }
import org.w3c.dom.{ CDATASection => DomCDATASection }
import org.w3c.dom.{ Element => DomElement }
import org.w3c.dom.{ ProcessingInstruction => DomProcessingInstruction }
import org.w3c.dom.{ DOMException, NamedNodeMap, UserDataHandler, TypeInfo }
import javax.xml.XMLConstants.{DEFAULT_NS_PREFIX, NULL_NS_URI} 

abstract class ReadOnlyNode extends DomNode {
	protected[antixml] lazy val immutable = new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "Immutable")

	// Node methods
	def getNodeName(): String

	@throws(classOf[DOMException])
	def getNodeValue: String

	@throws(classOf[DOMException])
	def setNodeValue(nodeValue : String) = throw immutable

	def getNodeType : Short
	def getParentNode : DomNode
	def getChildNodes : DomNodeList
	def getFirstChild : DomNode
	def getLastChild : DomNode
	def getPreviousSibling : DomNode
	def getNextSibling : DomNode
	def getAttributes : NamedNodeMap
	def getOwnerDocument : DomDocument

	@throws(classOf[DOMException])
	def insertBefore(newChild: DomNode, refChild: DomNode) = throw immutable

	@throws(classOf[DOMException])
	def replaceChild(newChild: DomNode, oldChild: DomNode) = throw immutable

	@throws(classOf[DOMException])
	def removeChild(oldChild: DomNode) = throw immutable

	@throws(classOf[DOMException])
	def appendChild(newChild: DomNode) = throw immutable

	def hasChildNodes : Boolean

	def cloneNode(deep: Boolean) = throw immutable

	def normalize() = throw immutable
	def isSupported(feature : String, version : String) : Boolean
	def getNamespaceURI() : String
	def getPrefix() : String

	@throws(classOf[DOMException])
	def setPrefix(prefix: String) { throw immutable }
	def getLocalName : String
	def hasAttributes : Boolean
	def getBaseURI : String
	
	// DOM level 3
	@throws(classOf[DOMException])
    def compareDocumentPosition(other : DomNode) : Short 
	@throws(classOf[DOMException])
    def getTextContent() : String
	@throws(classOf[DOMException])
    def setTextContent(textContext : String) = throw immutable
    def isSameNode(other : DomNode) : Boolean
    def lookupPrefix(namespaceURI : String) : String
    def isDefaultNamespace(namespaceURI : String) : Boolean
    def lookupNamespaceURI(prefix : String) : String
    def isEqualNode(arg : DomNode) : Boolean
    def getFeature(feature : String, version : String) : Object
    def setUserData(key : String, data : Object, handler : UserDataHandler) : Object = throw immutable
    def getUserData(key : String) : AnyRef
}
// List of abstract Node methods
//	// Node methods
//	def getNodeName(): String
//	@throws(classOf[DOMException])
//	def getNodeValue: String
//	def getNodeType : Short
//	def getParentNode : DomNode
//	def getChildNodes : DomNodeList
//	def getFirstChild : DomNode
//	def getLastChild : DomNode
//	def getPreviousSibling : DomNode
//	def getNextSibling : DomNode
//	def getAttributes : NamedNodeMap
//	def getOwnerDocument : DomDocument
//
//	def hasChildNodes : Boolean
//
//	def isSupported(feature : String, version : String) : Boolean
//	def getNamespaceURI() : String
//	def getPrefix() : String
//
//	@throws(classOf[DOMException])
//	def getLocalName : String
//	def hasAttributes : Boolean
//	def getBaseURI : String
//	// DOM level 3
//  @throws(classOf[DOMException])
//  def compareDocumentPosition(other : DomNode) : Short 
//  @throws(classOf[DOMException])
//  def getTextContent() : String
//  def isSameNode(other : DomNode) : Boolean
//  def lookupPrefix(namespaceURI : String) : String
//  def isDefaultNamespace(namespaceURI : String) : Boolean
//  def lookupNamespaceURI(prefix : String) : String
//  def isEqualNode(arg : DomNode) : Boolean
//  def getFeature(feature : String, version : String) : Object
//  def getUserData(key : String) : AnyRef

object EmptyChildren extends DomNodeList {
	def item(index : Int) : DomNode = null
	def getLength : Int = 0
}

trait ReadOnlyNamedNodeMap extends NamedNodeMap { 
	protected[antixml] lazy val immutable = new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "Immutable")
	
//	def getNamedItem (name : String) : Node

	@throws(classOf[DOMException])
	def setNamedItem (arg : DomNode) : DomNode = throw immutable 

	@throws(classOf[DOMException])
	def removeNamedItem (name : String) : DomNode = throw immutable  
	
//	def item (index : Int) : DomNode ;
//	def getLength () : Int ;
//	@throws(classOf[DOMException])
//	def getNamedItemNS (namespaceURI : String, localName : String) : Node 
	@throws(classOf[DOMException])
	def setNamedItemNS (arg : DomNode) : DomNode = throw immutable 
	@throws(classOf[DOMException])
	def removeNamedItemNS (namespaceURI : String, localName : String) : DomNode = throw immutable 
}
// abstract ReadOnlyNamedNodeMap members:
//	def getNamedItem (name : String) : Node
//	
//	def item (index : Int) : DomNode ;
//	def getLength () : Int ;
//	@throws(classOf[DOMException])
//	def getNamedItemNS (namespaceURI : String, localName : String) : Node 

object EmptyNamedNodeMap extends NamedNodeMap with ReadOnlyNamedNodeMap {
	def getNamedItem (name : String) : DomNode = null
	def item (index : Int) : DomNode = null
	def getLength () : Int = 0
	@throws(classOf[DOMException])
	def getNamedItemNS (namespaceURI : String, localName : String) : DomNode = null 
}

trait LeafNode extends ReadOnlyNode {
	def getChildNodes : DomNodeList = EmptyChildren
	def getFirstChild : DomNode = null
	def getLastChild : DomNode = null
	def getAttributes : NamedNodeMap = EmptyNamedNodeMap
	def hasChildNodes : Boolean = false
	def hasAttributes : Boolean = false
}

trait SimpleNode extends ReadOnlyNode {
	def isSupported(feature : String, version : String) : Boolean = false
	def getFeature(feature : String, version : String) : AnyRef = null
	def getUserData(key : String) : AnyRef = null
}

// List of abstract Node methods
//	// Node methods
//	def getNodeName(): String
//	@throws(classOf[DOMException])
//	def getNodeValue: String
//	def getNodeType : Short
//	def getParentNode : DomNode
//	def getChildNodes : DomNodeList
//	def getFirstChild : DomNode
//	def getLastChild : DomNode
//	def getPreviousSibling : DomNode
//	def getNextSibling : DomNode
//	def getAttributes : NamedNodeMap
//	def getOwnerDocument : DomDocument
//
//	def hasChildNodes : Boolean
//
//	def isSupported(feature : String, version : String) : Boolean
//	def getNamespaceURI() : String
//	def getPrefix() : String
//
//	@throws(classOf[DOMException])
//	def getLocalName : String
//	def hasAttributes : Boolean
//	def getBaseURI : String
//	// DOM level 3
//  @throws(classOf[DOMException])
//  def compareDocumentPosition(other : DomNode) : Short 
//  @throws(classOf[DOMException])
//  def getTextContent() : String
//  def isSameNode(other : DomNode) : Boolean
//  def lookupPrefix(namespaceURI : String) : String
//  def isDefaultNamespace(namespaceURI : String) : Boolean
//  def lookupNamespaceURI(prefix : String) : String
//  def isEqualNode(arg : DomNode) : Boolean
//  def getFeature(feature : String, version : String) : Object
//  def getUserData(key : String) : AnyRef

abstract class ReadOnlyCharacterDataAdapter extends ReadOnlyNode with DomCharacterData {
	// CharacterData
	@throws(classOf[DOMException])
    def getData() : String
    
    @throws(classOf[DOMException])
    def setData(data : String) = throw immutable

    def getLength() : Int

    @throws(classOf[DOMException])
    def substringData(offset : Int, count : Int) : String
    @throws(classOf[DOMException])
    def appendData(arg : String) = throw immutable

    @throws(classOf[DOMException])
    def insertData(offset : Int, arg : String) = throw immutable
	
    @throws(classOf[DOMException])
    def deleteData(offset : Int, count : Int) = throw immutable
    
	@throws(classOf[DOMException])
    def replaceData(offset : Int, count : Int, arg : String) = throw immutable
}
//	List of abstract methods from CharacterData
//	@throws(classOf[DOMException])
//  def getData() : String
//  def getLength() : Int
//  @throws(classOf[DOMException])
//  def substringData(offset : Int, count : Int) : String

abstract class ReadOnlyTextAdapter extends ReadOnlyCharacterDataAdapter with DomText with LeafNode {
  @throws(classOf[DOMException])
  def splitText(offset : Int) : DomText = throw immutable
  
  def isElementContentWhitespace : Boolean
  def getWholeText() : String

  @throws(classOf[DOMException])
  def replaceWholeText(content : String) : DomText = throw immutable
}

abstract class ReadOnlyProcInstAdapter extends ReadOnlyNode with DomProcessingInstruction {
    def getTarget : String
    def getData : String

    @throws(classOf[DOMException])
    def setData(data : String) = throw immutable
}

abstract class ReadOnlyElementAdapter extends ReadOnlyNode with DomElement {
    def getTagName () : String;
    def getAttribute (name : String) : String
    @throws(classOf[DOMException])
    def setAttribute(name : String, value : String) = throw immutable
	@throws(classOf[DOMException])
    def removeAttribute (name : String) = throw immutable 

    def getAttributeNode (name : String) : DomAttr

    @throws(classOf[DOMException])
    def setAttributeNode (newAttr : DomAttr) : DomAttr = throw immutable 
	@throws(classOf[DOMException])
    def removeAttributeNode (oldAttr : DomAttr) : DomAttr = throw immutable

	def getElementsByTagName (name : String) : DomNodeList
	@throws(classOf[DOMException])
    def getAttributeNS(namespaceURI : String, localName : String) : String

	@throws(classOf[DOMException])
    def setAttributeNS(namespaceURI : String, qualifiedName : String, value : String) = throw immutable

	@throws(classOf[DOMException])
    def removeAttributeNS(namespaceURI : String, localName : String) = throw immutable

	@throws(classOf[DOMException])
    def getAttributeNodeNS(namespaceURI : String, localName : String) : DomAttr

	@throws(classOf[DOMException])
    def setAttributeNodeNS (newAttr: DomAttr) : DomAttr = throw immutable

	@throws(classOf[DOMException])
    def getElementsByTagNameNS(namespaceURI : String, localName : String) : DomNodeList

    def hasAttribute (name : String) : Boolean ;

	@throws(classOf[DOMException])
    def hasAttributeNS(namespaceURI : String, localName : String) : Boolean

    def getSchemaTypeInfo () : TypeInfo

	@throws(classOf[DOMException])
    def setIdAttribute(name : String, isId : Boolean) = throw immutable

	@throws(classOf[DOMException])
    def setIdAttributeNS(namespaceURI : String, localName : String, isId : Boolean) = throw immutable

	@throws(classOf[DOMException])
    def setIdAttributeNode(idAttr : DomAttr,isId : Boolean) = throw immutable
}

// List of abstract Element methods:
//  def getTagName () : String;
//  def getAttribute (name : String) : String
//  def getAttributeNode (name : String) : DomAttr
//	def getElementsByTagName (name : String) : DomNodeList
//	@throws(classOf[DOMException])
//  def getAttributeNS(namespaceURI : String, localName : String) : String
//	@throws(classOf[DOMException])
//  def getAttributeNodeNS(namespaceURI : String, localName : String) : DomAttr
//	@throws(classOf[DOMException])
//  def getElementsByTagNameNS(namespaceURI : String, localName : String) : DomNodeList
//  def hasAttribute (name : String) : Boolean ;
//	@throws(classOf[DOMException])
//  def hasAttributeNS(namespaceURI : String, localName : String) : Boolean
//  def getSchemaTypeInfo () : TypeInfo

// List of abstract Element methods:
//  def getTagName () : String;
//  def getAttribute (name : String) : String
//  def getAttributeNode (name : String) : DomAttr
//	def getElementsByTagName (name : String) : DomNodeList
//	@throws(classOf[DOMException])
//  def getAttributeNS(namespaceURI : String, localName : String) : String
//	@throws(classOf[DOMException])
//  def getAttributeNodeNS(namespaceURI : String, localName : String) : DomAttr
//	@throws(classOf[DOMException])
//  def getElementsByTagNameNS(namespaceURI : String, localName : String) : DomNodeList
//  def hasAttribute (name : String) : Boolean ;
//	@throws(classOf[DOMException])
//  def hasAttributeNS(namespaceURI : String, localName : String) : Boolean
//  def getSchemaTypeInfo () : TypeInfo

abstract class ReadOnlyDocAdapter extends ReadOnlyNode with DomDocument {

	@throws(classOf[DOMException])
	def createElement(tagName: String) = throw immutable

	def createDocumentFragment() = throw immutable

	def createTextNode(data: String) = throw immutable

	def createComment(data: String) = throw immutable

	@throws(classOf[DOMException])
	def createCDATASection(data: String) = throw immutable

	@throws(classOf[DOMException])
	def createProcessingInstruction(target: String, data: String) = throw immutable

	@throws(classOf[DOMException])
	def createAttribute(name: String) = throw immutable

	@throws(classOf[DOMException])
	def createEntityReference(name: String) = throw immutable

	@throws(classOf[DOMException])
	def importNode(importedNode: DomNode, deep: Boolean) = throw immutable

	@throws(classOf[DOMException])
	def createElementNS(namespaceURI: String, qualifiedName: String) = throw immutable

	@throws(classOf[DOMException])
	def createAttributeNS(namespaceURI: String, qualifiedName: String) = throw immutable

	@throws(classOf[DOMException])
	def setXmlStandalone(xmlStandalone: Boolean) = throw immutable
	def setXmlVersion(xmlVersion: String) = throw immutable
	def setStrictErrorChecking(strictErrorChecking: Boolean) = throw immutable
	def setDocumentURI(documentURI: String) = throw immutable
	@throws(classOf[DOMException])
	def adoptNode(source: DomNode) = throw immutable
	def normalizeDocument() = throw immutable
	@throws(classOf[DOMException])
	def renameNode(n: DomNode,	namespaceURI: String, qualifiedName: String) = throw immutable
}

// List Abstract Document methods: 
//	def getDoctype() = null
//	def getImplementation() = null
//
//	def getDocumentElement() = null
//
//	def getElementsByTagName(tagname: String) = null
//
//	def getElementsByTagNameNS(namespaceURI: String, localName: String) = null
//
//	def getElementById(elementId: String) = null
//	def getInputEncoding() = "UTF-8"
//	def getXmlEncoding() = "UTF-8"
//	def getXmlStandalone() = false
//	def getXmlVersion() = "1.0"
//	@throws(classOf[DOMException])
//	def getStrictErrorChecking() = false
//	def getDocumentURI() = null
//	def getDomConfig() = null

