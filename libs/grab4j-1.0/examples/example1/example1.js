var ret = "";
var title = document.searchElement("html/head/title");
ret += title.getInnerText();
var paragraphs = document.searchElements("html/body/p");
for (var i = 0; i < paragraphs.length; i++) {
  var text = paragraphs[i].getInnerText();
  ret += "\r\n" + text;
}

result = ret;
