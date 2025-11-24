# MiniSQL/TOON – Documentação do Projeto  
Disciplina: Paradigmas de Linguagens de Programação  
Aluno: José Weverton e Larissa Hora
Professor: Augusto Sampaio
Semestre: 2025.2  

---

# 1. Introdução

Este projeto implementa uma DSL inspirada em SQL, chamada **MiniSQL/TOON**, com suporte aos comandos:

- `INSERT`
- `SELECT`
- `UPDATE`
- `DELETE`

A linguagem é interpretada, com execução imperativa e persistência em arquivos `.toon`.

O objetivo principal é demonstrar, na prática, conceitos fundamentais de **Paradigmas de Linguagens de Programação**, como:

- análise léxica  
- parsing  
- construção de AST  
- execução imperativa  
- criação de DSL  
- persistência textual  

---

# 2. Objetivos do Projeto

Os objetivos são:

1. Criar uma linguagem simples de manipulação de dados.
2. Demonstrar a implementação de Lexer, Parser e AST.
3. Implementar interpretação imperativa em Java.
4. Construir persistência em arquivos no formato **TOON**.
5. Fornecer um REPL (Read–Eval–Print Loop) para execução contínua.
6. Promover entendimento prático da implementação de linguagens.

---

# 3. Motivação para o Paradigma Imperativo

O paradigma **imperativo** foi escolhido para implementação do interpretador por vários motivos pedagógicos:

### ✔ Correspondência com o funcionamento real de motores SQL
Internamente, comandos SQL são executados de forma imperativa:

- loop sobre linhas  
- verificação de condição  
- mutação de valores  
- gravação em arquivo

### ✔ Clareza na execução da AST
Cada comando vira uma sequência de passos bem definidos.

### ✔ Simplicidade para ensino de PLP
O paradigma imperativo é mais direto para representar mudanças de estado.

### ✔ Facilita persistência
O formato TOON é gravado sequencialmente, linha por linha.

**Conclusão:**  
O imperativo é ideal para o objetivo educacional deste projeto.

---

# 4. Arquitetura Geral

A arquitetura segue o pipeline clássico de construção de linguagens:

Entrada → Lexer → Parser → AST → Executor → Persistência

## 4.1. Lexer
Responsável por transformar texto em tokens:

- palavras reservadas  
- números  
- strings  
- identificadores  
- símbolos

Implementado manualmente, caractere a caractere.

## 4.2. Parser
Implementado como **recursive descent parser**.

Métodos principais:

- `parseInsert()`
- `parseSelect()`
- `parseUpdate()`
- `parseDelete()`

## 4.3. AST
Cada comando vira um nó específico da AST, por exemplo:

- InsertStmt  
- SelectStmt  
- UpdateStmt  
- DeleteStmt  

## 4.4. Executor
Interpreta a AST, realizando operações imperativas sobre tabelas:

- carregamento
- filtragem
- projeção
- alteração
- persistência

## 4.5. Persistência com TOON
Formato textual simples, exemplo:

COLUMNS: id,name,age
1 Ana 20
2 Carlos 25


Vantagens:

- fácil leitura  
- fácil parsing  
- ideal para projeto de PLP  

---

# 5. Especificação da Linguagem MiniSQL

## 5.1. INSERT

INSERT INTO users (id,name,age) VALUES (1, "Ana", 20);


## 5.2. SELECT
SELECT id,name FROM users;
SELECT * FROM users WHERE age = 20;


## 5.3. UPDATE
UPDATE users SET name="Maria" WHERE id=1;


## 5.4. DELETE


DELETE FROM users WHERE id=1;


---

# 6. REPL (Read–Eval–Print Loop)

O REPL:

1. lê comandos até encontrar `;`  
2. envia para o Lexer  
3. Parser gera AST  
4. Executor processa a instrução  
5. imprime o resultado

Vantagens:

- simula SQL shell  
- ideal para testes interativos  
- fácil de expandir  

---

# 7. Estruturas Internas

## 7.1. Tabelas
Representadas como:

```java
List<Map<String, Object>> rows;
List<String> columns;

7.2. WHERE

Apenas condições simples:

coluna = valor

7.3. Tipos

Números → Integer

Strings → String

8. UML Textual
8.1. Lexer
Lexer
 ├─ input: String
 ├─ pos: int
 ├─ current: char
 └─ tokenize(): List<Token>

8.2. Parser
Parser
 ├─ tokens: List<Token>
 ├─ index: int
 ├─ parseStatement()
 ├─ parseInsert()
 ├─ parseSelect()
 ├─ parseUpdate()
 └─ parseDelete()

8.3. AST
InsertStmt
SelectStmt
UpdateStmt
DeleteStmt

8.4. Executor
Executor
 ├─ executeInsert()
 ├─ executeSelect()
 ├─ executeUpdate()
 └─ executeDelete()

8.5. TOON Storage
ToonFileStorage
 ├─ load(table)
 ├─ save(table)
 └─ parse()

9. Testes Realizados

Inserção simples

Atualização

Deleção

Seleção total

Seleção filtrada

Persistência após reiniciar REPL

Erros de parsing (tokens inválidos, sintaxe incorreta)

Todos os testes resultaram conforme esperado.

10. Limitações

WHERE limitado a col = valor

Sem tipos booleanos

Sem operadores lógicos

Sem CREATE TABLE

Sem JOIN

Sem índices

Limitações intencionais para simplificação.

11. Possíveis Extensões

Suporte completo ao WHERE

Suporte a CREATE/DROP TABLE

Novos tipos de dados

JOINs básicos

Exportação CSV/JSON

Otimizador simples de consultas

Transações

12. Conclusão

O projeto MiniSQL/TOON demonstra:

construção completa de uma DSL

implementação prática dos conceitos de PLP

separação clara entre Lexer → Parser → AST → Executor

uso adequado do paradigma imperativo

criação de formato de persistência próprio


A solução é modular, extensível e adequada para fins didáticos.
