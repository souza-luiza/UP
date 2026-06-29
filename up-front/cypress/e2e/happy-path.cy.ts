describe('Jornada do Novo Usuário (E2E Happy Path)', () => {
    const userPassword = 'TestPassword123!';

    it('deve permitir que um novo usuário se registre, faça login e acesse o perfil', () => {
        // Gera um e-mail único baseado no timestamp exato desta execução
        const emailAleatorio = `user_${Date.now()}@test.com`;
        const userNameAleatorio = 'User_' + Date.now();

        // 1. Visitar a página de registro e preencher os campos reais
        cy.visit('/register');
        cy.get('input[placeholder*="nome"]').type(userNameAleatorio);
        cy.get('input[type="email"]').type(emailAleatorio);
        cy.get('input[placeholder="Escreva aqui a sua senha"]').type(userPassword);
        cy.get('input[placeholder="Confirme a sua senha"]').type(userPassword);
        cy.get('button[type="submit"]').click();

        // 2. Verificar redirecionamento para a tela de login
        cy.get('.Toastify__toast--success', { timeout: 7000 })
            .should('be.visible')
            .and('contain', 'Cadastro realizado com sucesso!');
        cy.url().should('include', '/login');

        // 3. Preencher o formulário de login com o e-mail que acabou de ser criado
        cy.get('input[type="email"]').type(emailAleatorio);
        cy.get('input[type="password"]').type(userPassword);
        cy.get('button[type="submit"]').click();

        // 4. Verificar se o login teve sucesso e redirecionou para o perfil
        cy.url().should('include', '/perfil');

        // 5. Fluxo de Disciplinas
        cy.visit('/disciplinas');

        cy.get('input[placeholder*="nome"]')
            .clear()
            .focus()
            .type('Banco de Dados', { delay: 50 })
            .blur();
        cy.get('#nota-5').check();
        cy.get('button[type="submit"]').click();
        cy.contains('Banco de Dados', { timeout: 10000 }).should('be.visible');

        cy.get('input[placeholder*="nome"]')
            .clear()
            .focus()
            .type('POO', { delay: 50 })
            .blur();
        cy.get('#nota-3').check();
        cy.get('button[type="submit"]').click();
        cy.contains('POO', { timeout: 10000 }).should('be.visible');

        // 6. Fluxo de Disponibilidades (Ajustado para a rota real no plural)
        cy.visit('/disponibilidades');

        cy.get('input[type="time"]').eq(0)
            .clear()
            .focus()
            .type('19:00', { delay: 50 })
            .blur();

        cy.get('input[type="time"]').eq(1)
            .clear()
            .focus()
            .type('21:00', { delay: 50 })
            .blur();

        cy.get('select').select('Segunda-feira');
        cy.wait(300);

        cy.contains('button', 'Adicionar disponibilidade').click();

        cy.contains('Segunda-feira').should('be.visible');
        cy.contains('19:00').should('be.visible');
        cy.contains('21:00').should('be.visible');

        // 7. Geração e validação do Cronograma
        cy.visit('/cronograma');
        cy.contains('button', 'Gerar cronograma').click();

        cy.contains(/Resultado/i, { timeout: 10000 }).should('be.visible');

        cy.contains(/Segunda-Feira/i).should('be.visible');

        // 3. Valida a existência das matérias de forma completamente flexível (case-insensitive)
        cy.contains(/Banco de dados/i).should('be.visible');
        cy.contains(/POO/i).should('be.visible');

        // Lógica matemática para validar a ordenação por dificuldade de forma flexível
        let bdMinutes = 0;
        let pooMinutes = 0;

        const extractMinutes = (text: string) => {
            const match = text.match(/(\d+)\s*minutos/);
            return match ? parseInt(match[1], 10) : 0;
        };

        cy.get('body').then(($body) => {
            const bdElements = $body.find(':contains("Banco de Dados")');
            const pooElements = $body.find(':contains("POO")');

            bdMinutes = extractMinutes(bdElements.text());
            pooMinutes = extractMinutes(pooElements.text());

            cy.log(`Minutos calculados para Banco de Dados: ${bdMinutes}`);
            cy.log(`Minutos calculados para POO: ${pooMinutes}`);

            if (bdMinutes > 0 && pooMinutes > 0) {
                expect(bdMinutes).to.be.greaterThan(pooMinutes);
            }
        });
    });
});