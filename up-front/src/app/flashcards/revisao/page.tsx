"use client";

import Button from "@/components/Button";
import Logo from "@/components/Logo";
import { useEffect, useState } from "react";
import {
    getFlashcardsForReview,
    logout,
    reviewFlashcard
} from "@/services/api";
import { toast } from "react-toastify";

type Flashcard = {
    id: number;
    question: string;
    answer: string;
    reviewLevel: number;
    nextReviewDate: string;
};

export default function RevisaoFlashcards() {
    const [showAnswer, setShowAnswer] = useState(false);
    const [flashcards, setFlashcards] = useState<Flashcard[]>([]);
    const [currentIndex, setCurrentIndex] = useState(0);

    const handleAnswerAction = async (correct: boolean) => {
        if (!currentFlashcard) return;

        try {
            await reviewFlashcard(currentFlashcard.id, correct);
            setShowAnswer(false);
            setCurrentIndex(current => current + 1);
        } catch {
            toast.error("Erro ao revisar flashcard");
        }
    };

    async function loadReviews() {
        try {
            const data = await getFlashcardsForReview();
            setFlashcards(data);
        } catch {
            toast.error("Erro ao carregar revisões");
        }
    }

    useEffect(() => {
        loadReviews();
    }, []);

    const currentFlashcard = flashcards[currentIndex];
    
    // Variáveis auxiliares para a barra de progresso visual
    const totalCards = flashcards.length;
    const currentNumber = currentIndex + 1;

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

            <main className="flex flex-col p-6 items-center justify-center flex-1 md:px-20">

                {!currentFlashcard ? (
                    <div className="text-center flex flex-col items-center gap-3 bg-white border border-dashed border-slate-300 p-8 rounded-md-custom max-w-sm shadow-sm animate-fadeIn">
                        <span className="text-4xl">🎉</span>
                        <p className="text-h4 font-bold text-black">
                            Nenhum flashcard para revisar hoje.
                        </p>
                        <p className="text-p text-neutral-500 font-medium">
                            Seu cronograma de repetição espaçada está 100% em dia!
                        </p>
                    </div>
                ) : (
                    <div className="w-full max-w-md bg-white border border-secondary-dark/20 rounded-md-custom shadow-md p-6 flex flex-col gap-6 transition-all duration-200">
                        
                        <div className="flex items-center justify-between border-b border-slate-100 pb-3">
                            <h3 className="text-h3 text-black font-semibold">
                                Revisão de Flashcards
                            </h3>
                            <span className="text-xs font-bold text-neutral-400 bg-slate-100 px-2 py-0.5 rounded-full">
                                {currentNumber} / {totalCards}
                            </span>
                        </div>

                        <div className="flex flex-col gap-1.5">
                            <span className="text-xs font-semibold text-neutral-400 uppercase tracking-wider">Pergunta</span>
                            <p className="text-h4 font-medium text-black whitespace-pre-wrap leading-snug">
                                {currentFlashcard.question}
                            </p>
                        </div>

                        {showAnswer ? (
                            <div className="flex flex-col gap-1.5 border-l-2 border-primary/50 pl-3 pt-1 mt-1 transition-all duration-300">
                                <span className="text-xs font-semibold text-neutral-400 uppercase tracking-wider">Resposta</span>
                                <p className="text-h4 text-neutral-800 whitespace-pre-wrap leading-snug">
                                    {currentFlashcard.answer}
                                </p>
                            </div>
                        ) : (
                            <div className="h-12 border-t border-transparent" />
                        )}

                        <div className="mt-2 border-t border-slate-100 pt-4 flex w-full justify-center">
                            {!showAnswer ? (
                                <Button 
                                    onClick={() => setShowAnswer(true)} 
                                    className="w-full"
                                    variant="primary"
                                >
                                    Mostrar Resposta
                                </Button>
                            ) : (
                                <div className="flex w-full gap-4">
                                    <Button 
                                        onClick={() => handleAnswerAction(true)} 
                                        className="flex-1"
                                        variant="success"
                                    >
                                        Acertei
                                    </Button>
                                    <Button 
                                        onClick={() => handleAnswerAction(false)} 
                                        className="flex-1"
                                        variant="danger"
                                    >
                                        Errei
                                    </Button>
                                </div>
                            )}
                        </div>

                    </div>
                )}

            </main>
        </div>
    );
}