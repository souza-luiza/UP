/// <reference types="cypress" />
/// <reference types="mocha" />

describe('Jornada de Revisão de Flashcards (E2E)', () => {
    const userEmail = `flashcard_user_${Date.now()}@example.com`;
    const userName = 'Flashcard User';
    const userPassword = 'password123';
    const subjectName = 'Ciência de Dados';
  
    beforeEach(() => {
      // --- Etapa de Setup: Registrar, Logar e Criar Disciplina ---
      cy.register(userName, userEmail, userPassword);
      cy.login(userEmail, userPassword);
  
      // Criar uma disciplina para associar os flashcards
      cy.visit('/disciplinas');
      cy.get('[data-cy="disciplina-nome-input"]').type(subjectName);
      cy.get('[data-cy="disciplina-dificuldade-select"]').select('4');
      cy.get('[data-cy="add-disciplina-button"]').click();
      cy.contains(subjectName).should('be.visible');
    });
  
    it('deve atualizar o nível de revisão corretamente ao acertar e errar um flashcard', () => {
      // --- 1. Etapa de Criação de Flashcards ---
      cy.visit('/flashcards');
  
      // Criar o primeiro flashcard (para o cenário de acerto)
      cy.get('[data-cy="flashcard-subject-select"]').select(subjectName);
      cy.get('[data-cy="flashcard-question-input"]').type('O que é Overfitting?');
      cy.get('[data-cy="flashcard-answer-input"]').type('É quando um modelo se ajusta demais aos dados de treino.');
      cy.get('[data-cy="add-flashcard-button"]').click();
      cy.contains('O que é Overfitting?').should('be.visible');
  
      // Criar o segundo flashcard (para o cenário de erro)
      cy.get('[data-cy="flashcard-subject-select"]').select(subjectName);
      cy.get('[data-cy="flashcard-question-input"]').clear().type('O que é Underfitting?');
      cy.get('[data-cy="flashcard-answer-input"]').type('É quando um modelo é simples demais e não captura a complexidade dos dados.');
      cy.get('[data-cy="add-flashcard-button"]').click();
      cy.contains('O que é Underfitting?').should('be.visible');
  
      // --- 2. Etapa de Revisão ---
      cy.visit('/revisao'); // Navega para a página de revisão
  
      // -- Cenário de Acerto --
      cy.contains('h2', 'O que é Overfitting?').should('be.visible');
      cy.get('[data-cy="show-answer-button"]').click();
      cy.contains('p', 'É quando um modelo se ajusta demais aos dados de treino.').should('be.visible');
      
      cy.get('[data-cy="review-correct-button"]').click();
      // Verifica a mensagem de sucesso que indica o próximo intervalo
      // Baseado na lógica do backend (nível 0 -> 1), o próximo intervalo é de 3 dias.
      cy.contains('Ótimo! Próxima revisão em 3 dias.').should('be.visible');
  
      // -- Cenário de Erro --
      // O próximo card deve ser exibido automaticamente
      cy.contains('h2', 'O que é Underfitting?').should('be.visible');
      cy.get('[data-cy="show-answer-button"]').click();
      cy.contains('p', 'É quando um modelo é simples demais').should('be.visible');
  
      cy.get('[data-cy="review-wrong-button"]').click();
      // Verifica a mensagem que indica que o card será revisado em breve
      // Baseado na lógica do backend, o nível é reiniciado para 0, com intervalo de 1 dia.
      cy.contains('Sem problemas! Vamos revisar este card novamente amanhã.').should('be.visible');
    });
  });