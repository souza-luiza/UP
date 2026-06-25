"use client";

import { useState } from "react";
import Button from "../../components/Button";
import Logo from "../../components/Logo";
import CardCronograma from "@/components/CardCronograma";
import { generateSchedule, logout } from "@/services/api";
import { toast } from "react-toastify";

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
            toast.success("Cronograma gerado com sucesso!");
        } catch (error) {
            toast.error("Erro ao gerar cronograma");
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

    return (
        <div className="flex min-h-screen flex-col bg-[#FAFAFA]">
            <header className="flex w-full bg-primary py-1.5 px-1 md:px-20 justify-between items-center">
                <Logo primaryColor={false}/>
                <nav className="flex gap-4 items-center">
                    <Button variant="secondary" onClick={logout}>
                        Sair da conta
                    </Button>
                </nav>
            </header>

            <main className="flex flex-col p-6 gap-8 md:px-20 max-w-7xl mx-auto w-full">
                <div>
                    <h2 className="text-h2 text-black font-semibold">Gerador de Cronogramas</h2>
                </div>

                <div className="bg-primary/20 border border-primary/30 rounded-md-custom p-6 flex flex-col sm:flex-row sm:items-center sm:justify-between gap-6 shadow-sm">
                    <div className="max-w-xl">
                        <p className="text-p text-neutral-800 font-medium">
                            Gere automaticamente sessões de estudo inteligentes de forma personalizada, com base nas suas disciplinas e disponibilidades salvas.
                        </p>
                    </div>
                    <div className="w-fit shrink-0">
                        <Button 
                            onClick={handleGenerateSchedule}
                            variant={schedule.length > 0 ? "outline" : "primary"}
                            disabled={loading || schedule.length > 0}
                            className={schedule.length > 0 ? "bg-neutral-300! text-neutral-400! border-transparent! cursor-not-allowed! hover:bg-neutral-200! active:scale-100!" : ""}
                        >
                            {loading ? "Gerando..." : schedule.length > 0 ? "Cronograma Gerado" : "Gerar cronograma"}
                        </Button>
                    </div>
                </div>

                <div className="flex flex-col gap-4">
                    <h4 className="text-h4 text-black font-semibold">Resultados obtidos</h4>

                    {schedule.length === 0 ? (
                        <div className="border border-dashed border-slate-300 p-8 rounded-md-custom text-center bg-white">
                            <p className="text-p text-neutral-500 italic">
                                Nenhum cronograma gerado ainda. Clique no botão acima para começar.
                            </p>
                        </div>
                    ) : (
                        <div className="flex flex-wrap items-stretch gap-6 flex-row self-stretch">
                            {groupedSchedule.map((item: any, index) => (
                                <CardCronograma
                                    key={index}
                                    day={item.day}
                                    sessions={item.sessions}
                                />
                            ))}
                        </div>
                    )}
                </div>
            </main>
        </div>
    )
}