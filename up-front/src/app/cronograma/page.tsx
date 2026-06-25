import Button from "../../components/Button";
import Logo from "../../components/Logo";
import CardCronograma from "@/components/CardCronograma";

export default function Cronogramas() {
    const cronogramaExemplo = [
        {
            day: "Segunda-feira",
            sessions: [
                { time: "14:00 - 16:00", subject: "Banco de Dados" },
                { time: "16:00 - 18:00", subject: "POO" }
            ]
        },
        {
            day: "Terça-feira",
            sessions: [
                { time: "08:00 - 10:00", subject: "Estrutura de Dados" }
            ]
        },
        {
            day: "Quarta-feira",
            sessions: [
                { time: "08:00 - 10:00", subject: "Estrutura de Dados" }
            ]
        },
        {
            day: "Quinta-feira",
            sessions: [
                { time: "08:00 - 10:00", subject: "Estrutura de Dados" }
            ]
        },
        {
            day: "Sexta-feira",
            sessions: [
                { time: "08:00 - 10:00", subject: "Estrutura de Dados" }
            ]
        },
        {
            day: "Sábado",
            sessions: [
                { time: "08:00 - 10:00", subject: "Estrutura de Dados" }
            ]
        },
        {
            day: "Domingo",
            sessions: [
                { time: "08:00 - 10:00", subject: "Estrutura de Dados" }
            ]
        }
    ];

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

            <main className="flex flex-col p-6 gap-4 md:px-20">
                <h2 className="text-h2 text-black font-semibold">Gerador de Cronogramas</h2>
                <p className="text-p">
                    Gere automaticamente sessões de estudo com base nas disciplinas e disponibilidades cadastradas.
                </p>
                <div>
                    <Button>
                        Gerar cronograma
                    </Button>
                </div>

                <h4 className="text-h4 text-black font-semibold">Resultados</h4>

                {cronogramaExemplo.length === 0 ? (
                    <p className="text-p">
                        Nenhum cronograma gerado ainda.
                    </p>
                ) : (

                    <div className="flex flex-wrap items-stretch gap-6 flex-col md:flex-row self-stretch">
                        {cronogramaExemplo.map((item, index) => (
                            <CardCronograma 
                                key={index}
                                day={item.day ?? ''}
                                sessions={item.sessions}
                            />
                        ))}
                    </div>
                    
                )}

            </main>

        </div>
    )
}