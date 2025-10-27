set dotenv-load

build:
  javac -d out/production/Creo -cp "lib/*" *.java

run:
  java -cp "out/production/Creo:lib/*" SimpleHTTPClientUI
