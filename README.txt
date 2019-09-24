-------!!! Инструкция по установке enChat !!!---------------

Для развертывания приложения необходимо наличие 3 компонентов:
 - Java Runtime Enviroment - среда выполнения Java приложения (jre-8uu....)
 - контейнер сервлетов Tomcat (apache-tomcat-8.5.45)
 - сервер базы данных, в данном случае MYSQL или PostgreSQL (установка/настройка баз данных полностью возлагается на Вас)

Ссылки для скачивания продуктов:
https://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html
https://tomcat.apache.org/download-80.cgi
https://downloads.mysql.com/archives/community/
https://www.postgresql.org/download/

ПослПоместите архив приложения в ..\Apache Software Foundation\Tomcat 8.5\webapps
Доступен ряд настроек по адресу ESN.war\WEB-INF\classes\properties - это единственный файл, в который можно вносить изменения
Директория файлового хранилища и служебных файлов располагается здесь: ESN.war\resources\data\[URL организации]
 