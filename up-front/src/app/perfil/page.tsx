import Button from "../../components/Button";
import CardInfos from "../../components/CardInfos";
import Logo from "../../components/Logo";

export default function Perfil() {
    return(
        <div className="flex min-h-screen flex-col">
            <header className="flex w-full bg-primary py-1.5 px-1 md:px-20 justify-between items-center">
            
                <Logo primaryColor={false}/>
        
                <nav className="flex gap-4 items-center">
                    <Button variant="secondary">
                    Sair da conta
                    </Button>
                </nav>
            
            </header>

            <main className="flex flex-col p-6 gap-5 md:px-20">
                <h2 className="text-h2 text-black font-semibold">Olá, [nome]</h2>
                <div className="flex items-stretch gap-6 flex-col md:flex-row self-stretch">
                    <CardInfos variant="disciplinas"/>
                    <CardInfos variant="disponibilidades"/>
                    <CardInfos variant="sessões"/>
                    <CardInfos variant="flashcards"/>
                </div>

                <div className="flex items-stretch gap-10 flex-col self-stretch">
                    <div>
                        <h3 className="text-h3 text-black font-semibold">Próximo passo</h3>
                        <div>
                            <p className="text-p text-black font-regular pb-2">Você já possui disciplinas e horários cadastrados.</p>
                            <Button>
                                Gerar cronograma
                            </Button>
                        </div>
                    </div>

                    <div>
                        <h3 className="text-h3 text-black font-semibold">Revisões pendentes</h3>
                        <div>
                            <p className="text-p text-black font-regular pb-2">5 flashcards aguardando revisão hoje.</p>
                            <Button>
                                Revisar Agora
                            </Button>
                        </div>
                    </div>
                </div>


            </main>

        </div>
    )
}