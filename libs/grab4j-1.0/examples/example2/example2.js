importClass(Packages.it.sauronsoftware.grab4j.example2.Item);

var currentPage = 1;
var doc = document;

var links = new Array();
var k = 0;

while (true) {
  var els = doc.searchElements(".../a(href=cmsitem.php?item=*)");
  for (var i = 0; i < els.length; i++) {
    links[k++] = els[i].getLinkURL()
  }
  currentPage++;
  var nextPageLink = doc.searchElement(".../a(href=?page<3d>" + currentPage + ")");
  if (nextPageLink != null) {
    doc = openDocument(nextPageLink.getLinkURL());
  } else {
    break;
  }
}

var items = new Array();
var c = 0;

for (var i = 0; i < k; i++) {
  doc = openDocument(links[i]);
  var contentElement = doc.searchElement(".../td(class=content)");
  if (contentElement != null) {
    var titleElement = contentElement.searchElement("h1[0]");
    var textElement = contentElement.searchElement("table[0]/tr/td");
    if (titleElement != null && textElement != null) {
      var item = new Item();
      item.setTitle(titleElement.getInnerText());
      item.setContents(textElement.getInnerText());
      items[c++] = item;
    }
  }
}

result = items;
