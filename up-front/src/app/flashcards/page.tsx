"use client";

import { useEffect, useState } from "react";
import { createFlashcard, getFlashcards, logout, deleteFlashcard } from "@/services/api";
import Button from "../../components/Button";
import Logo from "../../components/Logo";
import TextInput from "../../components/TextInput";
import CardFlashcard from "@/components/CardFlashcard";
import { toast } from "react-toastify";
import { useAuth } from "@/hooks/useAuth";

type Flashcard = {
    id: number;
    question: string;
    answer: string;
    reviewLevel: number;
    nextReviewDate: string;
};

export default function Flashcards() {

    useAuth();

    const [question, setQuestion] = useState('');
    const [answer, setAnswer] = useState('');
    const [flashcards, setFlashcards] = useState<Flashcard[]>([]);

    const handleSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();
        try {
            await createFlashcard(question, answer);
            
            // Limpa os campos apenas em caso de sucesso
            setQuestion("");
            setAnswer("");

            toast.success("Flashcard cadastrado com sucesso!");
            await loadFlashcards();
        } catch {
            toast.error("Erro ao cadastrar flashcard.");
        }
    };

    async function handleDelete(id: number) {

        try {

            await deleteFlashcard(id);

            toast.success("Flashcard removido com sucesso!");

            await loadFlashcards();

        } catch {

            toast.error("Erro ao remover flashcard.");

        }
    }

    useEffect(() => {
        loadFlashcards();
    }, []);

    async function loadFlashcards() {
        try {
            const data = await getFlashcards();
            setFlashcards(data);
        } catch {
            toast.error("Erro ao carregar flashcards");
        }
    }

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
                
                <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 w-full">
                    <h2 className="text-h2 text-black font-semibold">Flashcards</h2>
                    <div className="w-fit">
                        <Button variant="outline" href="/flashcards/revisao">
                            Revisar Flashcards
                        </Button>
                    </div>
                </div>

                <div className="bg-primary/20 border border-primary/30 rounded-md-custom p-6 flex flex-col gap-4 shadow-sm">
                    <h4 className="text-h4 text-neutral-800 font-medium">
                        Cadastre flashcards para revisar conteúdos utilizando repetição espaçada.
                    </h4>
                    
                    <form onSubmit={handleSubmit} className="flex flex-col items-start gap-5 w-full max-w-xl">
                        <div className="w-full">
                            <TextInput 
                                id="question"
                                label="Pergunta"
                                type="text"
                                placeholder="Digite a pergunta do flashcard"
                                value={question}
                                onChange={(e) => setQuestion(e.target.value)}
                                required
                            />
                        </div>

                        <div className="w-full">
                            <TextInput 
                                id="answer"
                                label="Resposta"
                                type="text"
                                placeholder="Digite a resposta do flashcard"
                                value={answer}
                                onChange={(e) => setAnswer(e.target.value)}
                                required
                            />
                        </div>

                        <div className="w-fit">
                            <Button type="submit" variant="primary">
                                Adicionar Flashcard
                            </Button>
                        </div>
                    </form>
                </div>

                <div className="flex flex-col gap-4">
                    <h4 className="text-h4 text-black font-semibold">Flashcards cadastrados</h4>
                    
                    {flashcards.length > 0 ? (
                        <div className="flex flex-wrap items-stretch gap-6 flex-row self-stretch">
                            {flashcards.map((flashcard) => (
                                <CardFlashcard
                                    key={flashcard.id}
                                    question={flashcard.question}
                                    answer={flashcard.answer}
                                    level={flashcard.reviewLevel}
                                    onDelete={() => handleDelete(flashcard.id)}
                                />
                            ))}
                        </div>
                    ) : (
                        <div className="border border-dashed border-slate-300 p-8 rounded-md-custom text-center bg-white">
                            <p className="text-p text-neutral-500 italic">
                                Nenhum flashcard cadastrado ainda. Preencha o formulário acima para criar o primeiro.
                            </p>
                        </div>
                    )}
                </div>
            </main>
        </div>
    )
}