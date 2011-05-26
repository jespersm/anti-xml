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
import org.w3c.dom.{ Element => DomElement }
import org.w3c.dom.{ DOMException, NamedNodeMap, UserDataHandler, TypeInfo }
import javax.xml.XMLConstants.{DEFAULT_NS_PREFIX, NULL_NS_URI} 

class DocNodeListAdapter(val nodes: Seq[DomNode]) extends DomNodeList {
    def item(index : Int) : DomNode = nodes(index)
    def getLength : Int = nodes.length
}

class ElemAdapter(val elem: Elem, private val parent : DomNode) extends ReadOnlyElementAdapter {
	def getNodeName(): String = elem.name

	@throws(classOf[DOMException])
	def getNodeValue: String = elem.name
	
	def getNodeType : Short = DomNode.ELEMENT_NODE
	def getParentNode : DomNode = parent
	lazy val getChildNodes : DomNodeList = {
	  val children : Seq[DomNode] = elem.children.map { 
	    case e : Elem => new ElemAdapter(e, this)
	    case cdata : CDATA => new CDATASectionAdapter(cdata.text, this)
	    case text : Text => new TextAdapter(text.text, this)
	    case er : EntityRef => null
	    case pi : ProcInstr => null
	  }
	  new DocNodeListAdapter(children)
	}
	def getFirstChild : DomNode = getChildNodes.item(0)
	def getLastChild : DomNode = getChildNodes.item(getChildNodes.getLength - 1)
	def getPreviousSibling : DomNode = null // TODO
	def getNextSibling : DomNode = null // TODO
	def getAttributes : NamedNodeMap = null // TODO
	def getOwnerDocument : DomDocument = parent.getOwnerDocument
	
	def hasChildNodes : Boolean = !( elem.attrs.isEmpty && elem.children.isEmpty)
	
	def isSupported(feature : String, version : String) : Boolean = false
	def getNamespaceURI() : String = {
	  elem.prefix.map { x => elem.scope.getOrElse(x, NULL_NS_URI) }.getOrElse(NULL_NS_URI) 
	}
	def getPrefix() : String = elem.prefix.getOrElse(DEFAULT_NS_PREFIX)
	
	@throws(classOf[DOMException])
	def getLocalName : String = elem.name
	def hasAttributes : Boolean = false
	def getBaseURI : String = "" // TODO
	// DOM level 3
	@throws(classOf[DOMException])
	def compareDocumentPosition(other : DomNode) : Short = 0 
	@throws(classOf[DOMException])
	def getTextContent() : String = "TODO"
	def isSameNode(other : DomNode) : Boolean = other.isInstanceOf[ElemAdapter] && (other.asInstanceOf[ElemAdapter].elem eq elem) 
	def lookupPrefix(namespaceURI : String) : String = DEFAULT_NS_PREFIX // TODO
	def isDefaultNamespace(namespaceURI : String) : Boolean = false  // TODO
	def lookupNamespaceURI(prefix : String) : String = "" // TODO
	def isEqualNode(arg : DomNode) : Boolean = arg.isInstanceOf[ElemAdapter] && (arg.asInstanceOf[ElemAdapter].elem eq elem)
	def getFeature(feature : String, version : String) : Object = null // TODO
	def getUserData(key : String) : AnyRef = null // TODO
	def getTagName () : String = elem.prefix.map { _ + ":" + elem.name }.getOrElse(elem.name)
	def getAttribute (name : String) : String = "" // TODO 
	def getAttributeNode (name : String) : DomAttr = null // TODO
	def getElementsByTagName (name : String) : DomNodeList = null // TODO
	@throws(classOf[DOMException])
	def getAttributeNS(namespaceURI : String, localName : String) : String = null // TODO
	@throws(classOf[DOMException])
	def getAttributeNodeNS(namespaceURI : String, localName : String) : DomAttr = null // TODO
	@throws(classOf[DOMException])
  	def getElementsByTagNameNS(namespaceURI : String, localName : String) : DomNodeList = null // TODO
	def hasAttribute (name : String) : Boolean = false // TODO
	@throws(classOf[DOMException])
	def hasAttributeNS(namespaceURI : String, localName : String) : Boolean = false // TODO
	def getSchemaTypeInfo () : TypeInfo = null // TODO
}

class TextAdapter(val data : String, private val parent : DomNode) extends ReadOnlyTextAdapter with SimpleNode {
	def isElementContentWhitespace : Boolean = data.matches("\\s*")
	def getWholeText() : String = data // TODO
	@throws(classOf[DOMException])
	def getData() : String = data
	def getLength() : Int = data.length
	@throws(classOf[DOMException])
	def substringData(offset : Int, count : Int) : String = data.substring(offset, count)
	
	// Node methods
	def getLocalName : String = "#text"
	def getNodeName: String = "#text"
	@throws(classOf[DOMException])
	def getNodeValue: String = data
	def getNodeType : Short = DomNode.TEXT_NODE
	def getParentNode : DomNode = parent
	def getPreviousSibling : DomNode = null // TODO
	def getNextSibling : DomNode = null // TODO
	def getOwnerDocument : DomDocument = parent.getOwnerDocument

	def getNamespaceURI() : String = ""
	def getPrefix() : String = ""
	
	def isEqualNode(arg : DomNode) : Boolean = arg.isInstanceOf[TextAdapter] && (arg.asInstanceOf[TextAdapter].data == data)
	
	@throws(classOf[DOMException])
	def compareDocumentPosition(other : DomNode) : Short = 0 
	@throws(classOf[DOMException])
    def getTextContent() : String = ""
	@throws(classOf[DOMException])
    def isSameNode(other : DomNode) : Boolean =  other.isInstanceOf[TextAdapter] && (other.asInstanceOf[TextAdapter].data == data)
    def lookupPrefix(namespaceURI : String) : String = ""
    def isDefaultNamespace(namespaceURI : String) : Boolean = false
    def lookupNamespaceURI(prefix : String) : String = ""
	def getBaseURI : String = parent.getBaseURI
}

class ProcInstrAdapter(val target : String, val data : String, private val parent : DomNode) extends ReadOnlyProcInstAdapter with LeafNode with SimpleNode {
    def getTarget : String = target 
    def getData : String = data

  // Node methods
	def getLocalName : String = target
	def getNodeName: String = target
	@throws(classOf[DOMException])
	def getNodeValue: String = data
	def getNodeType : Short = DomNode.TEXT_NODE
	def getParentNode : DomNode = parent
	def getPreviousSibling : DomNode = null // TODO
	def getNextSibling : DomNode = null // TODO
	def getOwnerDocument : DomDocument = parent.getOwnerDocument

	def getNamespaceURI() : String = ""
	def getPrefix() : String = ""

	def getBaseURI : String = "" // TODO
	  
	def isEqualNode(arg : DomNode) : Boolean = arg.isInstanceOf[TextAdapter] && (arg.asInstanceOf[TextAdapter].data == data)
	
	@throws(classOf[DOMException])
	def compareDocumentPosition(other : DomNode) : Short = 0 
	@throws(classOf[DOMException])
    def getTextContent() : String = ""
	@throws(classOf[DOMException])
    def isSameNode(other : DomNode) : Boolean =  other.isInstanceOf[TextAdapter] && (other.asInstanceOf[TextAdapter].data == data)
    def lookupPrefix(namespaceURI : String) : String = ""
    def isDefaultNamespace(namespaceURI : String) : Boolean = false
    def lookupNamespaceURI(prefix : String) : String = ""
}

class CDATASectionAdapter(data : String, parent : DomNode) extends TextAdapter(data, parent) with SimpleNode {
	override def getNodeType : Short = DomNode.CDATA_SECTION_NODE
}

class DocAdapter(val rootElement: Elem) extends ReadOnlyDocAdapter {

	// Node methods
	def getNodeName(): String = "<document>"

	@throws(classOf[DOMException])
	def getNodeValue: String = "yo"

	def getNodeType : Short = DomNode.DOCUMENT_NODE
	def getParentNode : DomNode = null
	def getChildNodes : DomNodeList = null;//new NodeListAdapter(documentElement);
	def getFirstChild : DomNode = null
	def getLastChild : DomNode = null
	def getPreviousSibling : DomNode = null
	def getNextSibling : DomNode = null
	def getAttributes : NamedNodeMap = null
	def getOwnerDocument : DomDocument = this

	def hasChildNodes : Boolean = true

	def isSupported(feature : String, version : String) : Boolean  = false
	def getNamespaceURI() : String = ""
	def getPrefix() : String = ""

	@throws(classOf[DOMException])
	def getLocalName : String = "document"
	def hasAttributes : Boolean = false
	def getBaseURI : String = ""
	
	@throws(classOf[DOMException])
	def compareDocumentPosition(other : DomNode) : Short = 0 
	@throws(classOf[DOMException])
    def getTextContent() : String = ""
	@throws(classOf[DOMException])
    def isSameNode(other : DomNode) : Boolean = false
    def lookupPrefix(namespaceURI : String) : String = ""
    def isDefaultNamespace(namespaceURI : String) : Boolean = false
    def lookupNamespaceURI(prefix : String) : String = ""
    def isEqualNode(arg : DomNode) : Boolean = false
    def getFeature(feature : String, version : String) : AnyRef = null
    def getUserData(key : String) : AnyRef = null

	// Document
	def getDoctype() = null
	def getImplementation() = null
	def getDocumentElement() = new ElemAdapter(rootElement, this)
	def getElementsByTagName(tagname: String) = null
	def getElementsByTagNameNS(namespaceURI: String, localName: String) = null
	def getElementById(elementId: String) = null
	def getInputEncoding() = "UTF-8"
	def getXmlEncoding() = "UTF-8"
	def getXmlStandalone() = false
	def getXmlVersion() = "1.0"
	@throws(classOf[DOMException])
	def getStrictErrorChecking() = false
	def getDocumentURI() = null
	def getDomConfig() = null
	  
	override def toString = rootElement.toString
}

// List of abstract node methods:
//	def getNodeName(): String
//
//	@throws(classOf[DOMException])
//	def getNodeValue: String
//
//	@throws(classOf[DOMException])
//	def setNodeValue(nodeValue : String) 
//
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
