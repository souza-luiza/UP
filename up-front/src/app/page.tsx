import Button from "./components/Button";
import Logo from "./components/Logo";

export default function Home() {
  return (
    <div className="w-full flex min-h-screen flex-col items-center justify-between">
      <header className="flex w-full bg-primary py-1.5 px-1 md:px-20 justify-between items-center">
        <Logo primaryColor={false}/>
        <nav className="flex gap-4 items-center">
          <Button variant="secondary">
            Cadastrar
          </Button>
          <Button variant="outline">
            Acessar conta
          </Button>
        </nav>
      </header>
      <main></main>
      <footer></footer>
    </div>
  );
}
