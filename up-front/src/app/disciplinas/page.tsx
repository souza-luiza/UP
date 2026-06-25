"use client";

import { useEffect, useState } from "react";
import { createSubject, deleteSubject, getSubjects, logout } from "@/services/api";
import Button from "../../components/Button";
import Logo from "../../components/Logo";
import TextInput from "../../components/TextInput";
import RadioInput from "../../components/RadioInput";
import CardDisciplinas from "../../components/CardDisciplinas";
import { toast } from "react-toastify";

type Subject = {
    id: number;
    name: string;
    difficulty: number;
};

export default function Disciplinas() {
    const [name, setName] = useState('');
    const [rating, setRating] = useState<number | undefined>(undefined);
    const [subjects, setSubjects] = useState<Subject[]>([]);

    const handleSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (!rating) return;
        try {
            await createSubject(name, rating);
            setName("");
            setRating(undefined);
            
            toast.success("Disciplina cadastrada com sucesso!");
            await loadSubjects();
        } catch {
            toast.error("Erro ao cadastrar disciplina.");
        }
    };

    async function handleDelete(id: number) {

        try {

            await deleteSubject(id);

            toast.success("Disciplina removida.");

            await loadSubjects();

        } catch {

            toast.error("Erro ao remover disciplina.");

        }

    }

    async function loadSubjects() {
        try {
            const data = await getSubjects();
            setSubjects(data);
        } catch {
            toast.error("Erro ao carregar disciplinas");
        }
    }

    useEffect(() => {
        loadSubjects();
    }, []);

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
                    <h2 className="text-h2 text-black font-semibold">Disciplinas</h2>
                </div>

                <div className="bg-primary/20 border border-primary/30 rounded-md-custom p-6 flex flex-col gap-4 shadow-sm">
                    <h4 className="text-h4 text-neutral-800 font-medium">
                        Cadastre as disciplinas que serão utilizadas na geração do cronograma.
                    </h4>
                    
                    <form onSubmit={handleSubmit} className="flex flex-col items-start gap-6 w-full max-w-xl">
                        <div className="w-full">
                            <TextInput 
                                id="name"
                                label="Nome da Disciplina"
                                type="text"
                                placeholder="Digite o nome da disciplina"
                                value={name}
                                onChange={(e) => setName(e.target.value)}
                                required
                            />
                        </div>

                        <div className="flex flex-col gap-2">
                            <span className="text-p font-medium text-neutral-800">Selecione uma dificuldade de 1 a 5:</span>
                            <div className="flex flex-row flex-wrap gap-4 bg-white p-3 rounded-md border border-slate-200/80 w-fit">
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

                        <div className="w-fit">
                            <Button type="submit" variant="primary">
                                Adicionar disciplina
                            </Button>
                        </div>
                    </form>
                </div>

                <div className="flex flex-col gap-4">
                    <h4 className="text-h4 text-black font-semibold">Disciplinas cadastradas</h4>
                    
                    {subjects.length > 0 ? (
                        <div className="flex flex-wrap items-stretch gap-6 flex-row self-stretch">
                            {subjects.map((subject) => (
                                <CardDisciplinas
                                    key={subject.id}
                                    name={subject.name}
                                    difficulty={subject.difficulty}
                                    onDelete={() => handleDelete(subject.id)}
                                />
                            ))}
                        </div>
                    ) : (
                        <p className="text-p text-neutral-500 italic py-4">
                            Nenhuma disciplina cadastrada ainda.
                        </p>
                    )}
                </div>
            </main>
        </div>
    )
}