----------------------------

Our customer request us to build a remote storage, such that he is able to access his important files from everywhere.

 

1. Propose a solutions

2. Justify the selected solution

3. Implement the selected solution in java. use github to share the code with us.

4. Provide an acceptance test suite that our customer is able to run such that he is convinced that you built what he requested. 

-------------------------------

Solutia pe care o propun este un webservice care sa ofere functionalitati de acces la storage(storage care poate fi filesystem local, baza de date, sistem de document management etc). Sistemul ofera functionalitati de autentificare, upload fisier, download fisier, browse storage, sterge fisier/director, creare director, cautare fisier dupa nume.

Aplicatia este dezvoltata in java utilizand spring si porneste de la urmatorele premise:
- catre client este vizibil un storage similar unui file system, pornind de la un folder radacina
- separatorul pentru structura de foldere si fisiere vizibila catre utilizator este "\".
- aplicatia mapeaza cererile utilizatorului pe storage-ul fizic la care accesul este reglementat prin interfata StorageManager. In functie de optiunea de storage fizic - file system, database, etc trebuie scrisa o implementare de StorageManager. Eu am facut una pentru file sistem local.
- aplicatia contine atat service-ul oferit cat si un client cu o interfata web minimala pentru testarea functionalitatilor. Se poate merge astfel pe doua variante - fie acces via browser la storage prin extinderea clientului web din aplicatie, fie implementarea de solutii specifice pentru sisteme de operare diferite prin utilizarea serviciilor REST.

Detalii de implementare
- cele doua componente - service si client - au cate un fisier de configurare in care se specifica user si parola pentru basic authentication , separatorul de cale pentru structura vizibila la utilizator si locatia de pe disc a storage-ului pentru service, respectiv  care este locatia service-ului si locatia folder-ului pentru fisierele downloadate.
- in implementarea StorageManager-ului se foloseste si un utilitar pentru concilierea cailor de fisiere de pe disc cu caile de fisiere din storage vizibile catre client(in functie de sistemul de operare File.separator poate fi diferit).
- upload-ul este limitat la 10Mb.
- cautarea permite folosirea de wildcards(*,?)

Justificare
- solutia permite acces direct de oriunde via web browser
- pot fi dezvoltati clienti nativi pentru diverse sisteme de operare care sa utilizeze service-ul pus la dispozitie
 

Cum se testeaza

- se configureaza fisierele restservice-auth.properties si webclient-auth.properties
- se acceseza http://<host>:8080/RemoteStorage/ si se utilizeaza clientul web.

Plan de test pentru clientul web care acceseaza toate functionlitatile serviciului:
- TC1 - lista de fisiere dintr-un folder dat 
Comportament asteptat : La accesarea http://<host>:8080/RemoteStorage/ se incarca lista initiala de fisiere din folderul radacina. La fiecare navigare pe un folder vizibil din folderul curent se incarca lista de fisiere si foldere din folderul respectiv
- TC2 - adaugare fisier intr-un folder dat
Comportament asteptat : Din pagina listei de fisiere pentru un folder dat , prin apasarea butonului "Upload File" se afiseaza un formular pentru specificarea fisierului local ce se doreste incarcat pe storage. Dupa completarea numelui si apasarea butonului "Upload" se afiseaza rezultatul operatiunii si fisierul este vizibil la o noua incarcare a listei de fisiere a folderului curent din storage
- TC3 - creare folder intr-un folder dat
Comportament asteptat : Din pagina listei de fisiere pentru un folder dat , prin apasarea butonului "New Folder" se afiseaza un formular pentru specificarea numelui folder-ului ce se doreste a fi creat pe storage. Dupa completarea numelui si apasarea butonului "Create" se afiseaza rezultatul operatiunii si folderul este vizibil la o noua incarcare a listei de fisiere a folderului curent din storage
- TC4 - navigare in structura storage-ului
Comportament asteptat : Din pagina listei de fisiere pentru un folder dat , prin apasarea butonului "Up" se incrca lista pentru folderul parinte al folderului curent iar prin apasarea butonului "Navigate" corespunzator unui folder din lista se incarca lista de fisiere pentru folderul fiu
- TC5 - cautare dupa numele fisierului in storage
Comportament asteptat : Din pagina listei de fisiere pentru un folder dat prin completarea campului de cautare si apasarea butonului "Search" se afiseaza lista tuturor fisierelor din storage (nu doar din folderul curent)corespunzatoare cautarii. Exemplu: se cauta "Quick*" si se returneaza "folder1\folder2\Quick_Savings.pdf"
- TC6 - download fisier din storage
Comportament asteptat : Din pagina listei de fisiere pentru un folder dat , prin apasarea butonului "Download" corespunzator unui fisier din lista se salveaza fisierul sub acelasi nume in folderul temporar definit in fisierul de configurare

Ce ar putea fi adaugat/modificat

- autentificare mai complexa daca avem mai multi utilizatori sau cerinte de securitate mai mari
- eventual roluri diferite pentru adaugare/stergere si vizualizare/download
- nu exista validari
- interfata web poate fi imbunatatita
- clasele FileList si SearchList ar trebui unificate deoarece sunt similare, le utilizasem putin diferit la inceput si apoi nu le-am mai refactorizat.
- dupa cautare sa se poata si opera direct pe fisierele gasite ( download, stergere)
