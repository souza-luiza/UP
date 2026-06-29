"use client";

import { useEffect, useState } from "react";
import { createAvailability, getAvailabilities, logout, deleteAvailability } from "@/services/api";
import Button from "../../components/Button";
import Logo from "../../components/Logo";
import SelectInput from "../../components/SelectInput";
import TimeInput from "../../components/TimeInput";
import CardDisponibilidade from "../../components/CardDisponibilidade";
import { toast } from "react-toastify";
import { useAuth } from "@/hooks/useAuth";

type Availability = {
    id: number;
    dayOfWeek: string;
    startTime: string;
    endTime: string;
};

export default function Disponibilidades() {

    useAuth();

    const [dayOfWeek, setDayOfWeek] = useState("");
    const [startTime, setStartTime] = useState("");
    const [endTime, setEndTime] = useState("");
    const [availabilities, setAvailabilities] = useState<Availability[]>([]);

    const diasDaSemana = [
        { value: "MONDAY", label: "Segunda-feira" },
        { value: "TUESDAY", label: "Terça-feira" },
        { value: "WEDNESDAY", label: "Quarta-feira" },
        { value: "THURSDAY", label: "Quinta-feira" },
        { value: "FRIDAY", label: "Sexta-feira" },
        { value: "SATURDAY", label: "Sábado" },
        { value: "SUNDAY", label: "Domingo" },
    ];

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

    const handleSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();
        try {
            await createAvailability(dayOfWeek, startTime, endTime);
            setDayOfWeek("");
            setStartTime("");
            setEndTime("");

            toast.success("Disponibilidade cadastrada com sucesso!");
            await loadAvailabilities();
        } catch {
            toast.error("Erro ao cadastrar disponibilidade.");
        }
    };

    async function handleDelete(id: number) {

        try {

            await deleteAvailability(id);

            toast.success("Disponibilidade removida.");

            await loadAvailabilities();

        } catch {

            toast.error("Erro ao remover disponibilidade.");

        }

    }

    async function loadAvailabilities() {
        try {
            const data = await getAvailabilities();
            setAvailabilities(data);
        } catch {
            toast.error("Erro ao carregar disponibilidades");
        }
    }

    useEffect(() => {
        loadAvailabilities();
    }, []);

    return (
        <div className="flex min-h-screen flex-col">
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
                    <h2 className="text-h2 text-black font-semibold">Disponibilidades</h2>
                </div>

                <div className="bg-primary/20 border border-primary/30 rounded-md-custom p-6 flex flex-col gap-4 shadow-sm">
                    <h4 className="text-h4 text-neutral-800 font-medium">
                        Cadastre seus horários livres que serão utilizados na geração do cronograma.
                    </h4>
                    
                    <form onSubmit={handleSubmit} className="flex flex-col items-start gap-6 w-full max-w-xl">
                        <div className="w-full">
                            <SelectInput
                                id="dayOfWeek"
                                label="Dia da semana"
                                options={diasDaSemana}
                                value={dayOfWeek}
                                onChange={(e) => setDayOfWeek(e.target.value)}
                                required
                            />
                        </div>

                        <div className="flex flex-col sm:flex-row gap-4 items-start w-full">
                            <div className="w-full sm:w-1/2">
                                <TimeInput
                                    id="startTime"
                                    label="Horário inicial"
                                    value={startTime}
                                    onChange={(e) => setStartTime(e.target.value)}
                                    required
                                />
                            </div>
                            <div className="w-full sm:w-1/2">
                                <TimeInput
                                    id="endTime"
                                    label="Horário final"
                                    value={endTime}
                                    onChange={(e) => setEndTime(e.target.value)}
                                    required
                                />
                            </div>
                        </div>

                        <div className="w-fit">
                            <Button type="submit" variant="primary">
                                Adicionar disponibilidade
                            </Button>
                        </div>
                    </form>
                </div>

                <div className="flex flex-col gap-4">
                    <h4 className="text-h4 text-black font-semibold">Disponibilidades cadastradas</h4>
                    
                    {availabilities.length > 0 ? (
                        <div className="flex flex-wrap items-stretch gap-6 flex-row self-stretch">
                            {availabilities.map((availability) => (
                                <CardDisponibilidade
                                    key={availability.id}
                                    dayOfWeek={translateDay(availability.dayOfWeek)}
                                    startTime={availability.startTime}
                                    endTime={availability.endTime}
                                    onDelete={() => handleDelete(availability.id)}
                                />
                            ))}
                        </div>
                    ) : (
                        <p className="text-p text-neutral-500 italic py-4">
                            Nenhuma disponibilidade cadastrada ainda.
                        </p>
                    )}
                </div>
            </main>
        </div>
    )
}