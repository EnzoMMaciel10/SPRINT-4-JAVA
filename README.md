# 🚀 Apoia+ API — Sprint 4 (Java + Javalin)

API em **Java 17** com **Javalin 5** para o projeto **Apoia+**. Faz a ponte **frontend ↔ backend**, expõe **CRUD completo** (Pacientes, Consultas, Lembretes), trata erros, habilita **CORS** e oferece **health-check**.

**URL pública (Render):** https://sprint-4-java-1.onrender.com  
> **Dica:** o Javalin diferencia `/api` de **`/api/`** → use **sempre a barra final**.

---

## 📚 Sumário
- [✅ Status rápido](#-status-rápido)
- [🧭 Endpoints](#-endpoints)
  - [🧑‍⚕️ Pacientes](#️-pacientes)
  - [📅 Consultas & ⏰ Lembretes](#-consultas--⏰-lembretes)
- [🧩 Modelos](#-modelos)
  - [Paciente](#paciente)
  - [Consulta](#consulta)
  - [Lembrete](#lembrete)
- [🧪 Testes rápidos (curl)](#-testes-rápidos-curl)
- [🛠️ Rodando localmente](#️-rodando-localmente)
- [🌍 Deploy (Render)](#-deploy-render)
- [🔌 Integração com o Front-End](#-integração-com-o-front-end)
- [🗂️ Estrutura](#️-estrutura)
- [🧾 Rubrica atendida](#-rubrica-atendida)

---

## ✅ Status rápido
- **Health:** `GET /healthz` → `ok`  
- **Info da API:** `GET /api/` → `{"name":"Apoia+ API","version":"1.0.0"}`  
- **CORS:** habilitado para qualquer origem (facilita o front da sprint)

---

## 🧭 Endpoints

### 🧑‍⚕️ Pacientes
| Método | Caminho               | Descrição               |
|:-----:|------------------------|-------------------------|
| GET   | `/api/pacientes/`      | Lista pacientes         |
| POST  | `/api/pacientes/`      | Cria paciente           |
| GET   | `/api/pacientes/{id}`  | Busca paciente por id   |
| PUT   | `/api/pacientes/{id}`  | Atualiza paciente       |
| DELETE| `/api/pacientes/{id}`  | Remove paciente         |

### 📅 Consultas & ⏰ Lembretes
| Método | Caminho                                 | Descrição                           |
|:-----:|------------------------------------------|-------------------------------------|
| GET   | `/api/consultas/`                        | Lista consultas                     |
| POST  | `/api/consultas/`                        | Cria consulta                       |
| GET   | `/api/consultas/{id}`                    | Busca consulta por id               |
| PUT   | `/api/consultas/{id}`                    | Atualiza consulta                   |
| DELETE| `/api/consultas/{id}`                    | Remove consulta                     |
| POST  | `/api/consultas/{id}/confirmar`          | Marca **CONFIRMADA**                |
| POST  | `/api/consultas/{id}/ausente`            | Marca **AUSENTE**                   |
| POST  | `/api/consultas/{id}/lembretes`          | Gera **T-24h, T-2h, T-15min**       |
| GET   | `/api/consultas/{id}/lembretes`          | Lista lembretes da consulta         |

---

## 🧩 Modelos

### Paciente
```json
{
  "id": 1,
  "nome": "Maria Souza",
  "contato": "maria@email.com",
  "acessivelParaDigital": true,
  "ativo": true
}


Consulta
{
  "id": 10,
  "pacienteId": 1,
  "dataHora": "2025-11-30T14:30:00",
  "status": "AGENDADA",
  "linkSala": "https://meet.example/abc"
}

Lembrete
{
  "id": 100,
  "consultaId": 10,
  "tipo": "T24H | T2H | T15MIN",
  "agendamento": "2025-11-29T14:30:00",
  "enviado": false
}

🧪 Testes rápidos (curl)

Troque a URL se for testar localmente.

Health & Info

curl -s https://sprint-4-java-1.onrender.com/healthz
curl -s https://sprint-4-java-1.onrender.com/api/


Pacientes

# Criar
curl -s -X POST https://sprint-4-java-1.onrender.com/api/pacientes/ \
  -H "Content-Type: application/json" \
  -d '{"nome":"Maria Souza","contato":"maria@email.com","acessivelParaDigital":true,"ativo":true}'

# Listar
curl -s https://sprint-4-java-1.onrender.com/api/pacientes/

# Buscar
curl -s https://sprint-4-java-1.onrender.com/api/pacientes/1

# Atualizar
curl -s -X PUT https://sprint-4-java-1.onrender.com/api/pacientes/1 \
  -H "Content-Type: application/json" \
  -d '{"nome":"Maria A. Souza","contato":"maria@email.com","acessivelParaDigital":true,"ativo":true}'

# Remover
curl -i -X DELETE https://sprint-4-java-1.onrender.com/api/pacientes/1

Consultas & Lembretes

# Criar consulta (use um pacienteId existente)
curl -s -X POST https://sprint-4-java-1.onrender.com/api/consultas/ \
  -H "Content-Type: application/json" \
  -d '{"pacienteId":1,"dataHora":"2025-11-30T14:30:00","status":"AGENDADA","linkSala":"https://meet.example/abc"}'

# Listar consultas
curl -s https://sprint-4-java-1.onrender.com/api/consultas/

# Confirmar presença
curl -s -X POST https://sprint-4-java-1.onrender.com/api/consultas/10/confirmar

# Marcar ausente
curl -s -X POST https://sprint-4-java-1.onrender.com/api/consultas/10/ausente

# Gerar lembretes
curl -s -X POST https://sprint-4-java-1.onrender.com/api/consultas/10/lembretes

# Listar lembretes
curl -s https://sprint-4-java-1.onrender.com/api/consultas/10/lembretes

🛠️ Rodando localmente

Pré-requisitos: JDK 17 + Maven 3.9+

# build
mvn -DskipTests package

# executar (porta padrão 7070)
java -jar target/apoia-1.0.0.jar

# customizar porta
PORT=8080 java -jar target/apoia-1.0.0.jar


Health: http://localhost:7070/healthz

Info: http://localhost:7070/api/

🌍 Deploy (Render)

Tipo: Web Service (Docker)

Branch: deploy

Health Check Path: /healthz

Após o deploy, verá Your service is live 🎉 nos logs.

Em plano Free o serviço pode “hibernar” (primeiro acesso pode demorar).

🔌 Integração com o Front-End

Defina a base URL (ex.: Vite):

VITE_API_BASE=https://sprint-4-java-1.onrender.com


Exemplo de fetch:

fetch(`${import.meta.env.VITE_API_BASE}/api/pacientes/`)
  .then(r => r.json())
  .then(console.log);

🗂️ Estrutura
src/main/java/br/com/fiap/apoia/
 ├─ Application.java
 ├─ application/controller/ApiController.java
 ├─ domain/model/{Paciente,Consulta,Lembrete}.java
 ├─ domain/service/ApoiaService.java
 └─ infrastructure/
     ├─ Database.java
     └─ persistence/repository/{Jdbc*.java}

🧾 Rubrica atendida

✅ Verbos HTTP (GET/POST/PUT/DELETE) + CRUD

✅ Tratamento de erros (400/404/500 + handler global)

✅ CORS habilitado

✅ URL pública + health-check

✅ Boas práticas de consumo/exposição de API

´´´
