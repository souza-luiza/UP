describe('Jornada de Revisão de Flashcards (E2E)', () => {
    const userName = 'Flashcard User';
    const userPassword = 'password123';
    const subjectName = 'Ciência de Dados';
  
    beforeEach(() => {
        // Gera um e-mail único baseado no timestamp exato desta execução
        const userEmail = `flashcard_user_${Date.now()}@example.com`;

        // --- Etapa de Setup: Registrar, Logar e Criar Disciplina ---
        cy.register(userName, userEmail, userPassword);
        cy.login(userEmail, userPassword);
  
        // Criar uma disciplina para associar os flashcards
        cy.visit('/disciplinas');
        cy.get('input[placeholder*="nome"]').type(subjectName, { delay: 50 });
        cy.get('#nota-4').check();
        cy.get('button[type="submit"]').click();
        cy.contains(subjectName, { timeout: 10000 }).should('be.visible');
    });
  
    it('deve atualizar o nível de revisão corretamente ao acertar e errar um flashcard', () => {
        // --- 1. Etapa de Criação de Flashcards ---
        cy.visit('/flashcards');
  
        // Criar o primeiro flashcard (para o cenário de acerto)
        cy.get('select').select(subjectName); // Seleciona a matéria recém-criada
        cy.get('input[placeholder*="pergunta"], textarea[placeholder*="pergunta"]').type('O que é Overfitting?', { delay: 50 });
        cy.get('input[placeholder*="resposta"], textarea[placeholder*="resposta"]').type('É quando um modelo se ajusta demais aos dados de treino.', { delay: 50 });
        cy.contains('button', 'Adicionar flashcard').click();
        cy.contains('O que é Overfitting?').should('be.visible');
  
        // Criar o segundo flashcard (para o cenário de erro)
        cy.get('select').select(subjectName);
        cy.get('input[placeholder*="pergunta"], textarea[placeholder*="pergunta"]').clear().type('O que é Underfitting?', { delay: 50 });
        cy.get('input[placeholder*="resposta"], textarea[placeholder*="resposta"]').clear().type('É quando um modelo é simples demais e não captura a complexidade dos dados.', { delay: 50 });
        cy.contains('button', 'Adicionar flashcard').click();
        cy.contains('O que é Underfitting?').should('be.visible');
  
        // --- 2. Etapa de Revisão ---
        cy.visit('/revisao'); // Navega para a página de revisão
  
        // -- Cenário de Acerto --
        cy.contains('h2', 'O que é Overfitting?').should('be.visible');
        cy.contains('button', 'Mostrar resposta').click();
        cy.contains('É quando um modelo se ajusta demais aos dados de treino.').should('be.visible');
        
        cy.contains('button', 'Acertei').click(); // Ajuste o texto do botão de acerto se for diferente
        // Verifica a mensagem de sucesso que indica o próximo intervalo
        cy.contains('Ótimo! Próxima revisão em 3 dias.').should('be.visible');
  
        // -- Cenário de Erro --
        // O próximo card deve ser exibido automaticamente
        cy.contains('h2', 'O que é Underfitting?').should('be.visible');
        cy.contains('button', 'Mostrar resposta').click();
        cy.contains('É quando um modelo é simples demais').should('be.visible');
  
        cy.contains('button', 'Errei').click(); // Ajuste o texto do botão de erro se for diferente
        // Verifica a mensagem que indica que o card será revisado em breve
        cy.contains('Sem problemas! Vamos revisar este card novamente amanhã.').should('be.visible');
    });
});