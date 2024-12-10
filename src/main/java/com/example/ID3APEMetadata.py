import sys
import json
from datetime import datetime
sys.path.insert(0, 'C:/Users/aleja/Desktop/pruebafx/demo/src/main/java/com/example')
from mutagen.id3 import ID3, TDRC, ID3TimeStamp, TPE1, TIT2, TXXX, COMM

def datosmp3():
    file = ID3("C:/Users/aleja/Desktop/archivosprueba/file_example.mp3")
    print(file)
    print(file.getall("TDRC"))

def aplicarPlantillaID3(path, plantillaSeleccionada):
    with open('C:/Users/aleja/Desktop/pruebafx/demo/src/main/resources/com/example/plantillas.json', 'r') as file:
        data = json.load(file)
    file = ID3(path)
    print(file.getall("TDRC"))
    file.delete(delete_v1=True, delete_v2=True)
    for item in data:
        if item.get('name') == plantillaSeleccionada:
            metadatos = item.get('metadata')
            custom = item.get('custom')
    print(data)
    print(metadatos.get("author"))
    if not metadatos.get("author") == "":
        file.add(TPE1(encoding=3, text=[metadatos.get("author")]))
    if not metadatos.get("title") == "":
        file.add(TIT2(encoding=3, text=[metadatos.get("title")]))
    if not metadatos.get("comments") == "":
        file.add(COMM(encoding=3, lang="spa", desc="description", text=[metadatos.get("comments")]))
    if not metadatos.get("create date time") == "":
        fecha_hora = metadatos.get("create date time")
        fecha_objeto = datetime.strptime(fecha_hora, "%d-%m-%Y %H:%M:%S")
        fecha_formateada = fecha_objeto.strftime("%Y-%m-%dT%H:%M:%S")
        ano = metadatos.get("create date time").split("-")[2][:4]
        file.add(TDRC(encoding=0, text=[fecha_formateada]))
    for key,value in custom.items():
        file.add(TXXX(encoding=3, desc=key, text=[value]))
    file.save(v1=2)

aplicarPlantillaID3("C:/Users/aleja/Desktop/archivosprueba/file_example.mp3", "plantillaPrueba")
