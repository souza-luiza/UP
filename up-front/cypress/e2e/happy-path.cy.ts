describe('Jornada do Novo Usuário (E2E Happy Path)', () => {
    const userEmail = `testuser_${Date.now()}@example.com`;
    const userName = `Test User`;
    const userPassword = 'TestPassword123!';

    it('deve permitir que um novo usuário se registre, faça login e acesse o perfil', () => {
        // 1. Visitar a página de registro e preencher com seletores reais
        cy.visit('/register');
        cy.get('input[placeholder*="nome"]').type(userName);
        cy.get('input[type="email"]').type(userEmail);
        cy.get('input[placeholder="Escreva aqui a sua senha"]').type(userPassword);
        cy.get('input[placeholder="Confirme a sua senha"]').type(userPassword);
        cy.get('button[type="submit"]').click();

        // 2. Verificar redirecionamento para login
        cy.url().should('include', '/login');

        // 3. Preencher o formulário de login real
        cy.get('input[type="email"]').type(userEmail);
        cy.get('input[type="password"]').type(userPassword);
        cy.get('button[type="submit"]').click();

        // 4. Verificar se foi para a página correta de perfil
        cy.url().should('include', '/perfil');

        // 5. Visitar as demais páginas ajustando para as suas rotas reais
        cy.visit('/disciplinas');
        // ... restante do preenchimento das disciplinas ...

        // ATENÇÃO: Mudado para o plural '/disponibilidades' para bater com sua pasta real
        cy.visit('/disponibilidades');
        // ... restante do preenchimento das disponibilidades ...

        cy.visit('/cronograma');
        // ... restante da validação do cronograma ...
    });
});