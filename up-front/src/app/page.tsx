import Button from "./components/Button";
import Card from "./components/Card";
import Logo from "./components/Logo";

export default function Home() {
  return (
    <div className="flex min-h-screen flex-col items-center justify-between">
      <header className="flex w-full bg-primary py-1.5 px-1 md:px-20 justify-between items-center">

        <Logo primaryColor={false}/>

        <nav className="flex gap-4 items-center">
          <Button variant="outline" href="/register">
            Cadastrar
          </Button>
          <Button variant="secondary" href="/login"> 
            Acessar conta
          </Button>
        </nav>

      </header>

      <main className="flex flex-col items-center gap-5 self-stretch">

        <h1 className="text-h1 text-black font-semibold">UP: Universidade e Planejamento</h1>

        <p className="text-2xl text-center font-regular">
          Uma sistema de apoio aos estudos universitários <br/>
          para que você dê um UP na vida acadêmica!
        </p>

        <Button>
          Inicie sua jornada
        </Button>

      </main>

      <div className="flex flex-col justify-center items-center self-stretch gap-5">

        <h2 className="text-h2 text-black font-semibold">Conheça as ferramentas disponíveis no site</h2>

        {/*Cards*/}
        <div className="flex justify-center items-stretch px-5 gap-6 md:gap-[45px] flex-col md:flex-row self-stretch">
          <Card variant={"cronograma"} />
          <Card variant={"disciplinas"} />
          <Card variant={"disponibilidade"} />
          <Card variant={"flashcards"} />
        </div>

      </div>

      <footer className="bg-primary w-full text-center p-2">
        <p className="text-p text-black font-light">© 2026 UP. Todos os direitos reservados.</p>
      </footer>
    </div>
  );
}
