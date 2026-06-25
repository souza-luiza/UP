"use client";

import { useState } from "react";
import Button from "../components/Button";
import Logo from "../components/Logo";
import SelectInput from "../components/SelectInput";
import TimeInput from "../components/TimeInput";
import CardDisponibilidade from "../components/CardDisponibilidade";

export default function Disponibilidades() {
    const [dayOfWeek, setDayOfWeek] = useState("");
    const [startTime, setStartTime] = useState("");
    const [endTime, setEndTime] = useState("");

    const diasDaSemana = [
        { value: "MONDAY", label: "Segunda-feira" },
        { value: "TUESDAY", label: "Terça-feira" },
        { value: "WEDNESDAY", label: "Quarta-feira" },
        { value: "THURSDAY", label: "Quinta-feira" },
        { value: "FRIDAY", label: "Sexta-feira" },
        { value: "SATURDAY", label: "Sábado" },
        { value: "SUNDAY", label: "Domingo" },
    ];

    const handleSubmit = (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();
        // CHAMA PARA API
    };

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

            <main className="flex flex-col p-6 gap-3 md:px-20">
                <h2 className="text-h2 text-black font-semibold">Disponibilidades</h2>

                <div className="bg-primary rounded-md-custom p-3 flex flex-col gap-3">
                    <h4 className="text-h4 text-black font-semibold">Cadastre seus horários livres que serão utilizados na geração do cronograma.</h4>
                    
                    <form onSubmit={handleSubmit} className="flex flex-col items-start gap-4">
                        <SelectInput
                            id="dayOfWeek"
                            label="Dia da semana"
                            options={diasDaSemana}
                            value={dayOfWeek}
                            onChange={(e) => setDayOfWeek(e.target.value)}
                            required
                        />

                        <div className="flex flex-col md:flex-row gap-4 items-center">
                            <TimeInput
                                id="startTime"
                                label="Horário inicial"
                                value={startTime}
                                onChange={(e) => setStartTime(e.target.value)}
                                required
                            />

                            <TimeInput
                                id="endTime"
                                label="Horário final"
                                value={endTime}
                                onChange={(e) => setEndTime(e.target.value)}
                                required
                            />
                        </div>

                        <Button type="submit" variant="secondary">
                            Adicionar disponibilidade
                        </Button>
                    </form>
                </div>

                <h4 className="text-h4 text-black font-semibold">Disponibilidades cadastradas</h4>
                <div className="flex flex-wrap items-stretch gap-6 flex-col md:flex-row self-stretch">
                    <CardDisponibilidade dayOfWeek={"Segunda"} startTime={"14:00"} endTime={"18:00"} onDelete={() => console.log("deletar")} />
                    <CardDisponibilidade dayOfWeek={"Terça"} startTime={"14:00"} endTime={"18:00"} onDelete={() => console.log("deletar")} />
                </div>

            </main>

        </div>
    )
}