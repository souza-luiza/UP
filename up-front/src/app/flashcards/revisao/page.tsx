"use client";

import Button from "@/components/Button";
import Logo from "@/components/Logo";
import { useState } from "react";

export default function RevisaoFlashcards() {
    const [showAnswer, setShowAnswer] = useState(false);

    // Dados de exemplo do flashcard atual
    const currentFlashcard = {
        question: "O que é encapsulamento?",
        answer: "Ocultar detalhes internos de um objeto."
    };

    const handleAnswerAction = (success: boolean) => {
        if (success) {
            // CHAMA A API PARA SUBIR O NÍVEL DO CARD (Acertei)
            console.log("Usuário acertou!");
        } else {
            // CHAMA A API PARA ZERAR OU DIMINUIR O NÍVEL (Errei)
            console.log("Usuário errou!");
        }
        
        // Reseta o estado para o próximo flashcard vir oculto
        setShowAnswer(false);
    };

    return (
        <div className="flex min-h-screen flex-col">
            <header className="flex w-full bg-primary py-1.5 px-1 md:px-20 justify-between items-center">
                <Logo primaryColor={false}/>
                <nav className="flex gap-4 items-center">
                    <Button variant="secondary">
                        Sair da conta
                    </Button>
                </nav>
            </header>

            <main className="flex flex-col p-6 gap-6 items-center justify-center flex-1 md:px-20">

                {!currentFlashcard ? (
                    <div className="text-center flex flex-col items-center gap-2">
                        <span className="text-4xl">🎉</span>
                        <p className="text-h4 font-semibold text-black">
                            Nenhum flashcard para revisar hoje.
                        </p>
                        <p className="text-sm text-gray-500">
                            Seu cronograma de repetição espaçada está em dia!
                        </p>
                    </div>
                ) : (


                <div className="w-full max-w-md bg-white border border-gray-200 rounded-sm-custom shadow-md p-6 flex flex-col gap-6">
                    
                    <h3 className="text-h3 text-black font-semibold flex items-center gap-2 border-b border-gray-100 pb-3">
                        Revisão de Flashcards
                    </h3>

                    <div className="flex flex-col gap-1">
                        <span className="text-xs font-semibold text-gray-500 uppercase tracking-wider">Pergunta:</span>
                        <p className="text-h4 font-medium text-black whitespace-pre-wrap">
                            {currentFlashcard.question}
                        </p>
                    </div>

                    {showAnswer ? (
                        <div className="flex flex-col gap-1 border-t border-gray-100 pt-4 animate-fadeIn">
                            <span className="text-xs font-semibold text-gray-500 uppercase tracking-wider">Resposta:</span>
                            <p className="text-h4 text-gray-700 whitespace-pre-wrap">
                                {currentFlashcard.answer}
                            </p>
                        </div>
                    ) : null}

                    <div className="mt-4 border-t border-gray-100 pt-4 flex w-full justify-center">
                        {!showAnswer ? (
                            <Button 
                                onClick={() => setShowAnswer(true)} 
                                className="w-full"
                            >
                                Mostrar Resposta
                            </Button>
                        ) : (
                            <div className="flex w-full gap-4">
                                <Button 
                                    onClick={() => handleAnswerAction(true)} 
                                    className="flex-1 hover:bg-green-800"
                                    variant="success"
                                >
                                    Acertei
                                </Button>
                                <Button 
                                    onClick={() => handleAnswerAction(false)} 
                                    className="flex-1 hover:bg-red-800"
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