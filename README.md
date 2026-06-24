# 📚 UP 
Sistema de apoio aos estudos universitários que auxilia estudantes na organização da rotina acadêmica por meio de cronogramas de estudo gerados automaticamente e _flashcards_ com repetição espaçada.
> **UP = Universidade + Planejamento**  
> Um sistema para que você dê um **up** na vida acadêmica!

## 🎒 Funcionalidades

### 🔐 Autenticação e Segurança

O UP possui autenticação baseada em JWT, permitindo que cada estudante acesse sua conta de forma segura. O sistema conta com login, renovação de sessão por refresh token e logout, além de mecanismos de controle de acesso que garantem que disciplinas, disponibilidades, cronogramas e flashcards possam ser acessados e modificados apenas por seus respectivos proprietários.

### 📚 Gerenciamento de Disciplinas

O estudante pode cadastrar suas disciplinas e definir o nível de dificuldade de cada uma.

Essas informações são utilizadas pelo sistema para distribuir automaticamente o tempo de estudo.

### ⏰ Disponibilidade de Estudos

O estudante informa os períodos disponíveis para estudo durante a semana.

Essas disponibilidades servem como base para a geração automática do cronograma.

### 📅 Geração Automática de Cronogramas  

O sistema gera sessões de estudo automaticamente considerando:
- disciplinas cadastradas;
- dificuldade de cada disciplina;
- tempo disponível do estudante;
- limite máximo de duração por sessão
- tempo mínimo por disciplina.

O objetivo é distribuir o tempo de forma equilibrada e priorizar conteúdos mais difíceis.

### 🧠 Flashcards com Repetição Espaçada

Sistema de revisão onde cada flashcard possui um nível de revisão que é atualizado conforme o desempenho do estudante.

Quando o estudante acerta:
- o nível de revisão aumenta;
- o intervalo para a próxima revisão é ampliado.

Quando o estudante erra:
- o nível é reiniciado;
- a próxima revisão é antecipada.

Isso permite concentrar revisões nos conteúdos que apresentam maior dificuldade.

## ✅ Testes

O projeto possui testes automatizados para validação das regras de negócio e integração entre os componentes da aplicação.

### Testes unitários
- autenticação e autorização;
- geração automática de cronogramas;
- disponibilidades de estudo;
- disciplinas;
- flashcards;
- algoritmo de repetição espaçada.

### Testes de integração
- autenticação;
- usuários;
- cronograma;
- flashcards;
- disponibilidade.


## 🛠 Tecnologias
### Backend
- Java 21
- Spring Boot
- PostgreSQL

### Frontend
- Next.js
- React
- TypeScript

### Testes
- JUnit
- Mockito

----
## 🔮 Melhorias Futuras
- integração com horários de aula;
- calendário acadêmico;
- notificações de revisão;
- dashboard com métricas de estudo;
- estatísticas de desempenho dos flashcards.
