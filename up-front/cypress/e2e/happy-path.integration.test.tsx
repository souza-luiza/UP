import { render, screen, fireEvent, waitFor, act } from '@testing-library/react';
import { rest } from 'msw';
import { AppRouterContext } from 'next/dist/shared/lib/app-router-context.shared-runtime';
import { setupServer } from 'msw/node';
import Home from '../src/app/dashboard/page';
import RegisterPage from '../src/app/register/page';
import LoginPage from '../src/app/login/page';
import DisciplinasPage from '../src/app/disciplinas/page';
import DisponibilidadePage from '../src/app/disponibilidade/page';
import CronogramaPage from '../src/app/cronograma/page';

// Mock do Next.js router para simular navegação
const createMockRouter = (push) => ({
  push,
  replace: jest.fn(),
  // Adicione outros métodos do router que você usa, se necessário
});

// Configuração do Servidor Mock com MSW
const server = setupServer(
  // Mock para o endpoint de registro
  rest.post('/api/auth/register', (req, res, ctx) => {
    return res(ctx.status(201));
  }),
  // Mock para o endpoint de login
  rest.post('/api/auth/login', async (req, res, ctx) => {
    const { email } = await req.json();
    return res(ctx.json({ token: 'fake-jwt-token', user: { name: 'Test User', email } }));
  }),
  // Mock para os outros endpoints que serão chamados
  rest.post('/api/disciplinas', (req, res, ctx) => res(ctx.status(201))),
  rest.post('/api/disponibilidade', (req, res, ctx) => res(ctx.status(201))),
  rest.post('/api/cronograma/gerar', (req, res, ctx) => {
    // Retorna um cronograma mockado que segue a regra de negócio
    return res(ctx.json([
      { id: 1, disciplina: { nome: 'Banco de Dados' }, duracao: 90 },
      { id: 2, disciplina: { nome: 'POO' }, duracao: 30 },
    ]));
  })
);

beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());

describe('Jornada do Novo Usuário (Teste de Integração)', () => {
  it('deve permitir o registro, login, cadastro e geração de cronograma', async () => {
    const mockRouter = createMockRouter(jest.fn());

    // 1. Registro
    render(
      <AppRouterContext.Provider value={mockRouter as any}>
        <RegisterPage />
      </AppRouterContext.Provider>
    );
    fireEvent.change(screen.getByTestId('register-name-input'), { target: { value: 'Test User' } });
    fireEvent.change(screen.getByTestId('register-email-input'), { target: { value: 'test@example.com' } });
    fireEvent.change(screen.getByTestId('register-password-input'), { target: { value: 'password123' } });
    fireEvent.click(screen.getByTestId('register-submit-button'));

    // Aguarda a finalização do registro e verifica se o router foi chamado para redirecionar
    await waitFor(() => {
      expect(mockRouter.push).toHaveBeenCalledWith('/login');
    });

    // 2. Login
    // O `act` garante que a renderização e as atualizações de estado sejam processadas
    await act(async () => {
      render(
        <AppRouterContext.Provider value={mockRouter as any}>
          <LoginPage />
        </AppRouterContext.Provider>
      );
    });
    fireEvent.change(screen.getByTestId('login-email-input'), { target: { value: 'test@example.com' } });
    fireEvent.change(screen.getByTestId('login-password-input'), { target: { value: 'password123' } });
    fireEvent.click(screen.getByTestId('login-submit-button'));

    await waitFor(() => {
      expect(mockRouter.push).toHaveBeenCalledWith('/dashboard');
    });

    // 4. Cadastro de Disciplinas
    render(<DisciplinasPage />);
    // Disciplina difícil
    fireEvent.change(screen.getByTestId('disciplina-nome-input'), { target: { value: 'Banco de Dados' } });
    fireEvent.change(screen.getByTestId('disciplina-dificuldade-select'), { target: { value: '5' } });
    fireEvent.click(screen.getByTestId('add-disciplina-button'));
    await screen.findByText('Banco de Dados'); // Aguarda a disciplina aparecer na lista

    // Disciplina fácil
    fireEvent.change(screen.getByTestId('disciplina-nome-input'), { target: { value: 'POO' } }); // O clear() não é necessário aqui, pois renderizamos a página novamente
    fireEvent.change(screen.getByTestId('disciplina-dificuldade-select'), { target: { value: '3' } });
    fireEvent.click(screen.getByTestId('add-disciplina-button'));
    await screen.findByText('POO');

    // 5. Cadastro de Disponibilidade
    render(<DisponibilidadePage />);
    fireEvent.change(screen.getByTestId('disponibilidade-dia-select'), { target: { value: 'Segunda-feira' } });
    fireEvent.change(screen.getByTestId('disponibilidade-inicio-time-input'), { target: { value: '19:00' } });
    fireEvent.change(screen.getByTestId('disponibilidade-fim-time-input'), { target: { value: '21:00' } });
    fireEvent.click(screen.getByTestId('add-disponibilidade-button'));
    await screen.findByText(/Segunda-feira/);

    // 6. Geração de Cronograma
    render(<CronogramaPage />);
    fireEvent.click(screen.getByTestId('gerar-cronograma-button'));

    // Aguarda a resposta da API mockada e a renderização dos cards
    await waitFor(() => {
      expect(screen.getByText('Sessões de Estudo Geradas')).toBeInTheDocument();
    });

    const studySessions = await screen.findAllByTestId('sessao-estudo-card');
    
    let bdMinutes = 0;
    let pooMinutes = 0;

    studySessions.forEach(session => {
      const text = session.textContent || '';
      const match = text.match(/(\d+)\s*minutos/);
      const minutes = match ? parseInt(match[1], 10) : 0;

      if (text.includes('Banco de Dados')) {
        bdMinutes += minutes;
      } else if (text.includes('POO')) {
        pooMinutes += minutes;
      }
    });

    expect(bdMinutes).toBeGreaterThan(pooMinutes);
  });
});