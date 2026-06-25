"use client";

import { useState } from "react";
import Button from "../../components/Button";
import Logo from "../../components/Logo";
import CardCronograma from "@/components/CardCronograma";
import { generateSchedule } from "@/services/api";

export default function Cronogramas() {
    const [schedule, setSchedule] = useState<any[]>([]);
    const [loading, setLoading] = useState(false);

    function translateDay(day: string) {

        const days: Record<string, string> = {
            MONDAY: "Segunda-feira",
            TUESDAY: "Terça-feira",
            WEDNESDAY: "Quarta-feira",
            THURSDAY: "Quinta-feira",
            FRIDAY: "Sexta-feira",
            SATURDAY: "Sábado",
            SUNDAY: "Domingo"
        };

        return days[day] ?? day;
    }

    async function handleGenerateSchedule() {

        try {

            setLoading(true);

            const data = await generateSchedule();

            setSchedule(data.sessions);

        } catch (error) {

            alert("Erro ao gerar cronograma");

        } finally {

            setLoading(false);

        }
    }

    const groupedSchedule = Object.values(
        schedule.reduce((acc: any, session: any) => {

            const day = translateDay(session.dayOfWeek);

            if (!acc[day]) {

                acc[day] = {
                    day,
                    sessions: []
                };

            }

            acc[day].sessions.push({
                time: `${session.start} - ${session.end}`,
                subject: session.subjectName
            });

            return acc;

        }, {})
    );

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
                    <Button onClick={handleGenerateSchedule}>
                        {loading ? "Gerando..." : "Gerar cronograma"}
                    </Button>
                </div>

                <h4 className="text-h4 text-black font-semibold">Resultados</h4>

                {schedule.length === 0 ? (
                    <p className="text-p">
                        Nenhum cronograma gerado ainda.
                    </p>
                ) : (

                    <div className="flex flex-wrap items-stretch gap-6 flex-col md:flex-row self-stretch">
                        {groupedSchedule.map((item: any, index) => (
                            <CardCronograma
                                key={index}
                                day={item.day}
                                sessions={item.sessions}
                            />
                        ))}
                    </div>
                    
                )}

            </main>

        </div>
    )
}