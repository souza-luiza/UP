import Image from "next/image";
import Logo from "./components/Logo";

export default function Home() {
  return (
    <div className="w-full flex min-h-screen flex-col items-center justify-between">
      <header className="flex bg-primary py-[5px] px-[50px] justify-between items-center self-stretch">
        <Logo primaryColor={false}/>
      </header>
      <main></main>
      <footer></footer>
    </div>
  );
}
