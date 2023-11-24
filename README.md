# Task Management System


## Cómo usar el proyecto de uso?
El proyecto se encuentra elaborado en spring boot y Java 17, solo debe tener maven y Java instalado para el buen funcionamiento del sistema.
Utilizo flywaydb para el versionamiento de la base de datos, es decir, si gusta problarlo sin necesidad de utilizar la base de datos especificada en el proyecto, puede ser utilizado de forma local con Mysql. Al correr el proyecto correrá las migraciones y creará las tablas necesarias para el funcionamiento del sistema. Adicional fueron agregados algunos usuarios de pruebas.

## ¿Cómo manejarías la paginación y la ordenación de las tareas?
En la prueba técnica fueron agregada la paginación para usuario y tareas, JPA/Spring Boot nos ayuda con esa tarea, ya que solo debemos crear un objecto de tipo Pageable y configurar la cantidad de datos a mostrar y el ORM hará su magia.

## ¿Qué medidas tomarías para garantizar la seguridad de la aplicación?
Utilizaría OAuth2, para restringuir los accesos a usuarios no autenticadado, y encriptaría la llamada a mis servicios de punta a punta, sifrando la entrada y la salida, donde cada cliente tendría la forma de desifrar la información a partir de una llave pública.

## ¿Cómo escalarías esta aplicación si el número de usuarios y tareas aumentara significativamente?
Existen diferentes forma de garantizar la disponibilidad de los servicios, primero guardando todas las informaciones más consultada en Redis(Base de datos en memoria) para agilizar el tiempo de respuesta, adicional trabajaria de forma asincronica y teniendo la forma de particionar la carga.

## ¿Cómo gestionarías las actualizaciones de la base de datos sin tiempo de inactividad?
Hay dos formas, en la nube existe un servicio de “infrastructure as a service” donde la nube se encarga de actualizar la base de datos. Otra forma sería tener varios contenedores o instancias de bases de datos, luego actualizar cada instancia por separado y finalmente sincronizar los datos.
