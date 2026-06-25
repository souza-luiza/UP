"use client";

import { useState } from "react";
import Button from "../components/Button";
import Logo from "../components/Logo";
import TextInput from "../components/TextInput";
import RadioInput from "../components/RadioInput";
import CardDisciplinas from "../components/CardDisciplinas";

export default function Disciplinas() {
    const [name, setName] = useState('');
    const [rating, setRating] = useState<number | undefined>(undefined);

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
                <h2 className="text-h2 text-black font-semibold">Disciplinas</h2>

                <div className="bg-primary rounded-md-custom p-3 flex flex-col gap-3">
                    <h4 className="text-h4 text-black font-semibold">Cadastre as disciplinas que serão utilizadas na geração do cronograma.</h4>
                    
                    <form onSubmit={handleSubmit} className="flex flex-col items-start gap-4">
                        <TextInput 
                            id="name"
                            label="Nome da Disciplina"
                            type="text"
                            placeholder="Digite o nome da disciplina"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            required
                        />

                        <div>
                            <span className="text-h5">Selecione uma dificuldade de 1 a 5:</span>
                            <div className="flex flex-row gap-4">
                                {[1, 2, 3, 4, 5].map((num) => (
                                    <RadioInput
                                        key={num}
                                        id={`nota-${num}`}
                                        name="escala-nota"
                                        value={num}
                                        label={String(num)}
                                        checked={rating === num}
                                        onChange={(e) => setRating(Number(e.target.value))}
                                        required
                                    />
                                ))}
                            </div>
                        </div>

                        <Button type="submit" variant="secondary">
                            Adicionar disciplina
                        </Button>
                    </form>
                </div>

                <h4 className="text-h4 text-black font-semibold">Disciplinas cadastradas</h4>
                <div className="flex flex-wrap items-stretch gap-6 flex-col md:flex-row self-stretch">
                    <CardDisciplinas name={"Banco de Dados"} difficulty={3}/>
                    <CardDisciplinas name={"POO"} difficulty={3}/>
                </div>



            </main>

        </div>
    )
}