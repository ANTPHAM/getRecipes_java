This example 

1. Connects http://www.sauronsoftware.it/carlopelliccia/ and sets
   the fetched document as the current one.

2. Extracts from the current document the links to all the reported
   article pages (whose addresses are in the form cmsitem.php?item=*)

3. Looks for another index page. If there's another page it is fetched
   and it becomes the current one, and then returns to step 2.

4. Fetches every article page collected and extracts from it a
   title and a content.

5. Puts the retrieved contents in instances of the Item class.

6. Returns to the caller an array with the grabbed Item instances.


Questo esempio:

1. Si connette a http://www.sauronsoftware.it/carlopelliccia/ e
   imposta la pagina scaricata come quella di lavoro.

2. Estrae dalla pagina di lavoro tutti gli indirizzi dei collegamenti
   verso pagine di articolo (la cui forma è cmsitem.php?item=*).

3. Cerca un'altra pagina di indice degli articoli. Se ne trova una
   la scarica, la imposta come pagina di lavoro e torna al punto 2.

4. Scorre la lista collezionata dei collegamenti verso gli articoli,
   scarica ogni pagina di articolo e ne estrae un titolo ed un contenuto.

5. Per ogni pagina di articolo viene creato un oggetto Item che riporta
   le informazioni estratte.

6. Restituisce al codice chiamate un array di oggetti Item con le
   informazioni collezionate.
