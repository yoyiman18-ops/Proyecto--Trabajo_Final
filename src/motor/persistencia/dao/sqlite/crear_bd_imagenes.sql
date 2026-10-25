CREATE TABLE IF NOT EXISTS sheet (
    id_sheet INTEGER NOT NULL,
    path TEXT NOT NULL,
    extension TEXT NOT NULL,
    PRIMARY KEY (id_sheet)
);

CREATE TABLE IF NOT EXISTS recorte (
    id_sheet INTEGER NOT NULL,
    nombre_recorte TEXT NOT NULL,
    offset_x REAL NOT NULL,
    offset_y REAL NOT NULL,
    ancho REAL NOT NULL,
    alto REAL NOT NULL,
    FOREIGN KEY (id_sheet) REFERENCES sheet(id_sheet) ON DELETE CASCADE,
    PRIMARY KEY (id_sheet, nombre_recorte)
);

CREATE TABLE IF NOT EXISTS animacion (
    id_sheet INTEGER NOT NULL,
    nombre_animacion TEXT NOT NULL,
    es_bucle INTEGER NOT NULL,
    FOREIGN KEY (id_sheet) REFERENCES sheet(id_sheet) ON DELETE CASCADE,
    PRIMARY KEY (id_sheet, nombre_animacion)
);

CREATE TABLE IF NOT EXISTS frame (
    id_sheet INTEGER NOT NULL,
    nombre_animacion TEXT NOT NULL,
    nro_frame INTEGER NOT NULL,
    nombre_recorte TEXT NOT NULL,
    duracion INTEGER NOT NULL,
    FOREIGN KEY (id_sheet) REFERENCES sheet(id_sheet) ON DELETE CASCADE,
    FOREIGN KEY (id_sheet, nombre_animacion) REFERENCES animacion(id_sheet, nombre_animacion) ON DELETE CASCADE,
    FOREIGN KEY (id_sheet, nombre_recorte) REFERENCES recorte(id_sheet,nombre_recorte) ON DELETE CASCADE,
    PRIMARY KEY (id_sheet, nombre_animacion, nro_frame)
);

/*
nota: sqlite no revisa las claves foraneas por defecto, hay que poner la flag:
PRAGMA foreign_keys = on;
para que funcione la supresion en cascada y la verificacion de claves foraneas
*/