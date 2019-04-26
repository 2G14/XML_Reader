package xml_sample.printXML;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 読み込んだXMLを表示するクラス
 */
public class XMLPrinter {
  /**
   * Document List
   */
  private final List<Document> documents;
  /**
   * コンストラクタ
   */
  public XMLPrinter(final String... filenames) {
    this.documents = new ArrayList<Document>();
    DocumentBuilder builder;
    try {
      builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      for (final String filename : filenames)
        this.documents.add(builder.parse(Files.newInputStream(Paths.get(filename))));
    } catch (final ParserConfigurationException e) {
      e.printStackTrace();
    } catch (final SAXException e) {
      e.printStackTrace();
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }
  /**
   * 全てのDocumentを表示していく
   */
  public void readAll() {
    this.documents.forEach(this::read);
  }
  /**
   * Documentを表示していく
   * @param document 現在の見ているDocument
   */
  public void read(final Document document) {
    this.children(document.getDocumentElement(), 0);
    System.out.println();
  }
  /**
   * 属性の表示
   * @param node 現在見ているノード
   */
  private void attribute(final Node node) {
    NamedNodeMap attributes;
    if (node.hasAttributes()) {
      attributes = node.getAttributes();
      for (int i = 0; i < attributes.getLength(); i++)
        System.out.print(" " + attributes.item(i).getNodeName() + "=\"" + attributes.item(i).getNodeValue() + "\"");
    }
  }
  /**
   * 子ノードを表示していく
   * @param node     現在見ているノード
   * @param tabCount 階層表示のためのカウント
   */
  private void children(final Node node, final int tabCount) {
    System.out.print(this.tab(tabCount) + "<" + node.getNodeName());
    this.attribute(node);
    System.out.println(">");

    NodeList children;
    if (node.hasChildNodes()) {
      children = node.getChildNodes();
      for (int i = 0; i < children.getLength(); i++)
        switch (children.item(i).getNodeType()) {
          case Node.TEXT_NODE:
            System.out.println(this.tab(tabCount + 1) + children.item(i).getNodeValue());
            break;
          case Node.ELEMENT_NODE:
            this.children(children.item(i), tabCount + 1);
            break;
          default:
            break;
        }
    }
  }
  /**
   * 階層構造を表すためのタブを返す
   * @param  tabCount タブの数
   * @return          タブの数 * 2スペース
   */
  private String tab(final int tabCount) {
    String tab = "";
    for (int i = 0; i < tabCount; i++)
      tab += "  ";
    return tab;
  }
  /**
   * メイン
   * @param args
   */
  public static void main(final String[] args) {
    final XMLPrinter printer = new XMLPrinter("sample1.xml", "sample2.xml"); // 見たいファイルを複数入力
    printer.readAll();
  }
}

