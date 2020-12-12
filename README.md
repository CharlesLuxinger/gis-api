# GIS API
Aplicação para gerenciamento de parceiros e indicações com base na coordenadas geográficas do usuário.

## Repositórios
- [Github](https://github.com/CharlesLuxinger/gis-api)
- [Docker Hub](https://hub.docker.com/r/charlesluxinger/gis-api)

## Dependências
- [Maven 3.6.3](https://maven.apache.org/download.cgi)
- [Java 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [Git & Bash](https://git-scm.com/downloads)
- [Docker](https://www.docker.com/products/docker-desktop) & [Docker Compose](https://docs.docker.com/compose/install/)
- [curl](https://curl.se/)
## Instruções
#### - Subindo a api em Docker
1) Através de um terminal clone o projeto e acessa a pasta docker-local do projeto:
    ```shell
    $ git clone git@github.com:CharlesLuxinger/gis-api.git
    $ cd gis-api/docker-local
    ```
2) Iniciei a aplicação e o database com:
    ```shell
    $ bash startup-api.sh
    ```
3) Caso deseje popular o database com dados de testes:
    ```shell
    $ bash initial-data.sh
    ```
#### - Subindo api através da IDE
1) Caso não possuo o clone do projeto realize o 1º passo acima
2) Iniciei e o database com:
    ```shell
    $ bash startup-database.sh
    ```
3) Caso deseje popular ao database realize o 3º passo acima
4) As variáveis de ambiente necessárias para conexão com ao database estão no arquivo `.env`na raiz do projeto
___
### Utilizando a aplicação via swagger
- Acesse em um browser a url `http://localhost:9000/api/v1`
---
### Utilizando a aplicação via curl:
- Para inserir um novo parceiro:
    ```shell
    $ curl -X POST 'http://localhost:9000/api/v1/partner' -d '{"ownerName": "João Pé de Porco", "document": "321.132.312/000230", "tradingName": "O Boteco da Esquina",  "coverageArea": {"type": "MultiPolygon", "coordinates": [[[[-43.36556, -22.99669], [-43.36539, -23.01928 ], [-46.62135, -23.61440], [-43.36556, -22.99669]]]]}, "address": {"type": "Point", "coordinates": [-43.297337, -23.013538]}}' -H 'Content-type: application/json'
    ```
  Response:
    ```json
  {
        "id": "5fd4093f208437af28770cbf",
        "ownerName": "João Pé de Porco",
        "document": "321.132.312/000230",
        "tradingName": "O Boteco da Esquina",
        "coverageArea": {
            "type": "MultiPolygon",
            "coordinates": [
                [
                    [
                        [-43.36556, -22.99669],
                        [-43.36539, -23.01928],
                        [-46.62135, -23.61440],
                        [-43.36556, -22.99669]
                    ]
                ]
            ]
        },
        "address": {
            "type": "Point",
            "coordinates": [-43.297337, -23.013538]
        }
  }
    ```
- Busca parceiro por ID:
    ```shell
    $ curl -X GET 'http://localhost:9000/api/v1/partner/{id}' -H 'Content-type: application/json'
    ```
  Response:
    ```json
  {
        "id": "5fd4093f208437af28770cbf",
        "ownerName": "João Pé de Porco",
        "document": "321.132.312/000230",
        "tradingName": "O Boteco da Esquina",
        "coverageArea": {
            "type": "MultiPolygon",
            "coordinates": [
                [
                    [
                        [-43.36556, -22.99669],
                        [-43.36539, -23.01928],
                        [-46.62135, -23.61440],
                        [-43.36556, -22.99669]
                    ]
                ]
            ]
        },
        "address": {
            "type": "Point",
            "coordinates": [-43.297337, -23.013538]
        }
  }
    ```
- Buscar um parceiro mais próximo e que esteja dentro da área de cobertura, passando os parâmetros de longitude `long` e latitude `lat`:
    ```shell
    $ curl -X GET 'http://localhost:9000/api/v1/partner?lat=-23.61440&long=-46.62135'
    ```
  Response:
    ```json
  {
        "id": "5fd4093f208437af28770cbf",
        "ownerName": "João Pé de Porco",
        "document": "321.132.312/000230",
        "tradingName": "O Boteco da Esquina",
        "coverageArea": {
            "type": "MultiPolygon",
            "coordinates": [
                [
                    [
                        [-43.36556, -22.99669],
                        [-43.36539, -23.01928],
                        [-46.62135, -23.61440],
                        [-43.36556, -22.99669]
                    ]
                ]
            ]
        },
        "address": {
            "type": "Point",
            "coordinates": [-43.297337, -23.013538]
        }
  }
    ```
---
## Teste unitários:
- Acesse a raiz do projeto através do terminal:
    ```shell
    $ mvn clean test -U
    ```
---
## Health Check
- Acesse em um browser a url `http://localhost:9000/api/v1/health`
---
## Disponibilizando a API ao público
### Database
- Se faz necessário disponibilizar acesso ao MongoDB com as seguintes configurações realizadas:
1) Criar database `gis`
1) Criar collection `partners`
2) Criar os indexes: 
    ```
    db.partners.createIndex({"address.coordinates":"2dsphere"})
   
    db.partners.createIndex({"document":1}, { unique: true })
   ```
### API
- Para realizar deploy da aplicação em um cloud provider como por exemplo AWS Cold Build, realize um clone do projeto e utilize os arquivos `docker-compose.yml` e `Dockerfile` na raiz do projeto, considerando que o arquivo `.env` deva possuir as variáveis de acesso ao database.