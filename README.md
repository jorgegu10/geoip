# Geolocation Ip 
Proyecto SpringBoot para leer un archivo csv con spring batch y almacenarlo en una BD Postgres
- [![playversion](https://img.shields.io/badge/Springboot-2.7.3-brightgreen)](https://spring.io/projects/spring-boot) Spring Boot 2.7.3
- [![mavenversion](https://img.shields.io/badge/Java-11-red)](https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html) Java 11


## Proyecto

El proyecto está dividido en una estructura de paqueteria con arquitectura limpia, de la siguiente manera:

- **aplication:** Es donde se establece la configuración del servicio, configuración de beans y jobs.
- **domain:** En este módulo está nuestros objetos del dominio y nuestra lógica de negocio
- **infraestructure:** Es la paqueteria donde esta los adaptadores y entrypoints donde estará implementado en este caso nuestro controlador REST

## Introducción y Tecnologia
El desarrollo usa Spring Batch para procesar un archivo CSV y almacenarlo en Base de datos. Spring Batch nos permite poder automatizar tareas que implica el procesamiento de mucha información, está compuesto de un Job que es la tarea, y cada job esta compuesto por uno o muchos pasos, cada paso tiene una actividad de lectura, uno de procesamiento y otro de escritura. Esto trabaja bajo un JobRepository que es el compoenente que nos permite comunicar  spring batch con la persistencia de la base de datos para ver el estado de los job, y existe un JobLuncher que nos permite invocar a la ejecución del job.

Spring Batch es un framework ligero enfocado específicamente en la creación de procesos batch y proporciona gran cantidad de componentes que dan soporte a las diferentes necesidades que implican: trazas, transaccionalidad, contingencia, paralelismo, lectura y escritura de datos.
![image](https://user-images.githubusercontent.com/32180242/188990657-aeed6741-064f-4059-8457-37ec673987ca.png)

## Preguntas del proyecto
**1. ¿Cuál sería para usted una forma óptima de leer los datos en el archivo de entrada
para su almacenamiento?**

Para este caso, para una forma óptima de lectura de datos se ha decidido usar SpringBatch, que con el elemento de Reader permite la lectura de datos del archivo de entrada, leyendo porciones de datos convirtiendolos en un *chunck* (fragmento de la información leida de un archivo) que son definidos por unos parametros y mapeados en una lista de items, y si este lanza una excepción puede informar del registro problematico y el número de linea. De esta manera nuestros registros estaran mapeadas en una lista de items de nuestro objeto DTO con los atributos definidos, y si existe un processor, estos chunks pasan al processor para que los trate.

Para este proyecto se ha decidido leer los datos del archivo en chunks de 10000 registros para ser procesados y finalmente insertados en BD
![image](https://user-images.githubusercontent.com/32180242/189013405-71377c34-d5f0-4545-ae91-c3f8bcef73a9.png)
![image](https://user-images.githubusercontent.com/32180242/188996807-c8885788-718b-4451-a502-8fd00916c85a.png)

Implementación de Reader con LineMapper definiendo la estructura del archivo y el delmitador de mis datos, para finalmente ser mapeados en un objeto DTO
![image](https://user-images.githubusercontent.com/32180242/188996687-af31eaa8-cf2b-4ac4-800e-451c1afe9026.png)

Para implementar un mejor rendimiento, y procesamiento concurrente se define un taskExecutor que nos permite definir el número de hijos para procesar.

![image](https://user-images.githubusercontent.com/32180242/189014445-87154b82-4064-4fe3-acd4-2dcebd86b829.png)



**2. ¿Cuál sería el diseño de los objetos en la capa de persistencia?**

Al usar Spring batch, en la implementación de la capa de persistencia se debe utilizar una base de datos transaccional debido a los requisitos de transaccionalidad de JobRepository, que es el componente que nos permite comunicar spring batch con la persistencia de la base de datos para ver el estado de los jobs. 
En este caso usamos Postgresql, pero siendo una arquitectura limpia, podemos usar cualquier tipo de base de datos tan solo cambiando la implementación.
 
Una vez se arranca una aplicación Spring Batch, se establece una conexión con la base de datos que contiene el esquema de tablas que utiliza el framework.
Estas tablas nos permiten auditar el proceso, conocer su estado, la fecha de ejecución, duración del proceso, si ha sido exitoso o si ha habido errores.

*BATCH_JOB_INSTANCE:* Esta tabla almacena toda la información referente a las instancias de Job.

*BATCH_JOB_EXECUTION_PARAMS:* En esta tabla encontraremos los parámetros que recibe cada job en formato clave/valor.

*BATCH_JOB_EXECUTION:* Se almacena aquí la información referente a cada ejecución del Job.

*BATCH_STEP_EXECUTION:* Esta tabla almacena información sobre cada step del job. 

![image](https://user-images.githubusercontent.com/32180242/189013948-908e260e-f891-4a3f-8f32-70271c72db0a.png)

En la misma capa de persistencia creamos la entidad modelo de la tabla geolocationip donde vamos almacenar los datos leídos del archivo y su respectivae interfaz de JPARepository, colocando como llaves primarias compuestas, ip_from y ip_to.

**3. ¿Cuál sería para usted una forma óptima de almacenar los datos leídos en la capa
de persistencia?**

Como se trata de gran cantidad de registros almacenados en el archivo CSV, debemos pensar en almacenar nuestros datos en un proceso asincrono, con concurrencia y tolerante a fallos. Springbatch nos ofrece en su elemento de writer un componente que escribe el chunk de elementos, este método de escritura es responsable de procesar los elementos completamente y guardarlos en base de datos llamando nuestro método de la interfaz de la capa de persistencia, en este caso también existirá un rollback de este chunck de elementos si ocurre un error.

![image](https://user-images.githubusercontent.com/32180242/189011347-2174aa45-e478-4c3e-8725-9c4ceda28e21.png)
![image](https://user-images.githubusercontent.com/32180242/189011322-9d345ee2-4a17-4e98-8721-39a1ffdfe13c.png)


4. ¿Cuál sería para usted la forma correcta de exponer la operación de consulta de
datos para una IP?

TODO


## Ejecutar el proyecto

Para ejecutar la aplicación localmente se deben ejecutar los siguientes comandos:
```shell
git clone https://github.com/jorgegu10/geoip.git
cd geoip
mvn clean package
```
## Dockerizar el proyecto:
Para construir el ambiente en docker se deben ejecutar las siguientes líneas, este ya construye la base de datos de prueba.
```shell
cd geoip
docker-compose build
```
## Ejecutar el contenedor de docker:
Para construir el ambiente en docker se deben ejecutar las siguientes líneas
```shell
cd geoip
docker-compose up
```
## Probar la aplicación:
### [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
En está URL se encontrará la documentación en formato swagger del API de Geolocation IP, donde encontraremos 3 métodos.

Encontramos 2 operaciónes para procesar de archivo, uno sincrono y otro asincrono.

![image](https://user-images.githubusercontent.com/32180242/189017073-b7a4c8e6-a7da-43eb-b426-285c09704213.png)

Y la tercera operación de consulta por IP  

![image](https://user-images.githubusercontent.com/32180242/189017156-99bc5960-e83e-45d1-b682-9dbc5ecd4fde.png)

