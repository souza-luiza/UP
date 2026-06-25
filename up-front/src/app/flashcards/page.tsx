"use client";

import { useEffect, useState } from "react";
import {createFlashcard, getFlashcards} from "@/services/api";
import Button from "../../components/Button";
import Logo from "../../components/Logo";
import TextInput from "../../components/TextInput";
import CardFlashcard from "@/components/CardFlashcard";

type Flashcard = {
    id: number;
    question: string;
    answer: string;
    reviewLevel: number;
    nextReviewDate: string;
};

export default function Flashcards() {
    const [question, setQuestion] = useState('');
    const [answer, setAnswer] = useState('');
    const [flashcards, setFlashcards] = useState<Flashcard[]>([]);

    const handleSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();
        try {

            await createFlashcard(
                question,
                answer
            );

            setQuestion("");
            setAnswer("");

            await loadFlashcards();

        } catch {

            alert("Erro ao cadastrar flashcard.");

        }

        // Limpa os campos após o envio
        setQuestion('');
        setAnswer('');
    };

    useEffect(() => {
        loadFlashcards();
    }, []);

    async function loadFlashcards() {

        try {

            const data = await getFlashcards();

            setFlashcards(data);

        } catch {

            console.error("Erro ao carregar flashcards");

        }

    }

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
                <h2 className="text-h2 text-black font-semibold">Flashcards</h2>

                <Button variant="outline">
                    Revisar Flashcards
                </Button>

                <div className="bg-primary rounded-md-custom p-3 flex flex-col gap-3">
                    <h4 className="text-h4 text-black font-semibold">Cadastre flashcards para revisar conteúdos utilizando repetição espaçada.</h4>
                    
                    <form onSubmit={handleSubmit} className="flex flex-col items-start gap-4">
                        <TextInput 
                            id="question"
                            label="Pergunta"
                            type="text"
                            placeholder="Digite a pergunta do flashcard"
                            value={question}
                            onChange={(e) => setQuestion(e.target.value)}
                            required
                        />

                        <TextInput 
                            id="answer"
                            label="Resposta"
                            type="text"
                            placeholder="Digite a resposta do flashcard"
                            value={answer}
                            onChange={(e) => setAnswer(e.target.value)}
                            required
                        />

                        <Button type="submit" variant="secondary">
                            Adicionar Flashcard
                        </Button>
                    </form>
                </div>

                <h4 className="text-h4 text-black font-semibold">Flashcards cadastrados</h4>
                <div className="flex flex-wrap items-stretch gap-6 flex-col md:flex-row self-stretch">
                    {flashcards.map((flashcard) => (
                        <CardFlashcard
                            key={flashcard.id}
                            question={flashcard.question}
                            answer={flashcard.answer}
                            level={flashcard.reviewLevel}
                            onDelete={() => console.log("deletar")}
                        />
                    ))}
                </div>



            </main>

        </div>
    )
}