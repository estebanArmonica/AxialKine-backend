
# Sistema de suscripciones Tienda Web - AxialKine

Este repositorio contiene el backend de sistema de suscripción axialkine el cual tiene el fin de realizar una API con integraciones con metodos de pago Transbank y DTE para generación de factura SII simple y reportes en excel con Apache POI.

# Especificaciones del proyecto

>[!IMPORTANTE]
El proyecto utiliza diferentes herramientras para generar archivos en excel y en PDF consultar la documentación de apache POI  [Documentación Apache POI](https://poi.apache.org/apidocs/index.html).

Adicionalmente este proyecto se llevo a cabo con las diferentes herramientas:

- [ ] Java 21
- [ ] Apache Netbeans IDE 24 + librerias
- [ ] iText 5 [Archivo jar para crear PDF](https://repo1.maven.org/maven2/com/itextpdf/itextpdf/5.5.13.2/itextpdf-5.5.13.2.jar)
- [ ] Apache POI 3.7 [Archivo en GitHub](https://github.com/estebanArmonica/JoyeriaInventario/tree/main/dependecias)
- [ ] PostgreSQL Conector 42.7.5 [Archivo en GitHub](https://github.com/estebanArmonica/JoyeriaInventario/tree/main/dependecias)

# Levantamiendo del Backup en PostgreSQL
el proyecto esta creado con bases de datos PostgreSQL, para esto existe un archivo llamada [respaldo.backup](https://github.com/estebanArmonica/JoyeriaInventario/tree/main/Ejecutable%20y%20bakup), este Backup es que tiene que utilizar en pgAdmin para restaurar la base de datos.

- Primero en pgAdmin cre un nuevo `Owner` llamado `esteban` con la contraseña `armonica`


- cree una nueva base de datos llamada `inventario`, por defecto esta en el puerto `5432`


# Contribución

Siéntase libre de enviar solicitudes de extracción con mejoras, correcciones de errores o nuevas características. Las contribuciones son bienvenidas.


# Problemas

Si encuentra algún problema o tiene alguna pregunta, no dude en abrir un problema en este repositorio.

¡Gracias por su interés en este proyecto!
