import Button from "../components/Button";
import Card from "../components/Card";
import Logo from "../components/Logo";

export default function Home() {
  return (
    <div className="flex min-h-screen flex-col items-center justify-between">
      <header className="flex w-full bg-primary py-1.5 px-1 md:px-20 justify-between items-center">

        <Logo primaryColor={false}/>

        <nav className="flex gap-4 items-center">
          <Button variant="outline" href="/login"> 
            Acessar conta
          </Button>
          <Button variant="secondary" href="/register">
            Cadastrar
          </Button>
        </nav>

      </header>

      <main className="flex flex-col items-center gap-5 p-5 self-stretch">

        <h1 className="text-h1 text-black font-semibold">UP: Universidade e Planejamento</h1>

        <p className="text-2xl text-center font-regular">
          Uma sistema de apoio aos estudos universitários <br/>
          para que você dê um UP na vida acadêmica!
        </p>

        <Button href="/register">
          Inicie sua jornada
        </Button>

      </main>

        {/*Cards*/}
        <div className="flex justify-between items-stretch gap-6 p-5 md:p-1 md:px-20 flex-col md:flex-row self-stretch">
          <Card variant={"cronograma"} />
          <Card variant={"disciplinas"} />
          <Card variant={"disponibilidade"} />
          <Card variant={"flashcards"} />
        </div>

      <footer className="bg-primary w-full text-center p-2">
        <p className="text-p text-black font-light">© 2026 UP. Todos os direitos reservados.</p>
      </footer>
    </div>
  );
}
