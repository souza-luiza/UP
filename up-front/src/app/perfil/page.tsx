"use client";

import Button from "../../components/Button";
import CardInfos from "../../components/CardInfos";
import Logo from "../../components/Logo";
import { useEffect, useState } from "react";
import {getMe, getSubjects, getAvailabilities, getFlashcards, getFlashcardsForReview} from "@/services/api";
import { logout } from "@/services/api";
import { toast } from "react-toastify";
import { useAuth } from "@/hooks/useAuth";

export default function Perfil() {

    useAuth();

    const [name, setName] = useState("");

    const [subjects, setSubjects] = useState(0);

    const [availabilities, setAvailabilities] = useState(0);

    const [flashcards, setFlashcards] = useState(0);

    const [reviews, setReviews] = useState(0);

    const nextStep = (() => {

        if (subjects === 0) {
            return {
                message: "Cadastre suas disciplinas para começar.",
                button: "Cadastrar disciplinas",
                href: "/disciplinas"
            };
        }

        if (availabilities === 0) {
            return {
                message: "Informe seus horários disponíveis.",
                button: "Cadastrar disponibilidades",
                href: "/disponibilidades"
            };
        }

        return {
            message: "Gere seu cronograma de estudos.",
            button: "Gerar cronograma",
            href: "/cronograma"
        };

    })();

    useEffect(() => {

        loadPerfil();

    }, []);

    async function loadPerfil() {

        try {

            const [
                user,
                subjectList,
                availabilityList,
                flashcardList,
                reviewList
            ] = await Promise.all([

                getMe(),
                getSubjects(),
                getAvailabilities(),
                getFlashcards(),
                getFlashcardsForReview()

            ]);

            setName(user.name);

            setSubjects(subjectList.length);

            setAvailabilities(availabilityList.length);

            setFlashcards(flashcardList.length);

            setReviews(reviewList.length);

        } catch {

            toast.error("Erro ao carregar dados de perfil");

        }

    }

    return(
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
                <h2 className="text-h2 text-black font-semibold">Olá, {name}</h2>
                
                <div className="flex flex-wrap gap-4 items-stretch">
                    <CardInfos variant="disciplinas" value={subjects} />
                    <CardInfos variant="disponibilidades" value={availabilities} />
                    <CardInfos variant="flashcards" value={flashcards} />
                </div>

                <div className="flex items-start gap-8 flex-col self-stretch">
                    
                    <div className="w-full bg-primary/20 border border-slate-100 p-5 rounded-md-custom flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
                        <div>
                            <h3 className="text-h3 text-black font-semibold mb-1">Próximo passo</h3>
                            <p className="text-p text-black font-regular">
                                {nextStep.message}
                            </p>
                        </div>
                        <div className="w-fit">
                            <Button href={nextStep.href}>
                                {nextStep.button}
                            </Button>
                        </div>
                    </div>

                    <div className="w-full bg-primary/20 border border-slate-100 p-5 rounded-md-custom flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
                        <div>
                            <h3 className="text-h3 text-black font-semibold mb-1">Revisões pendentes</h3>
                            {reviews > 0 ? (
                                <p className="text-p text-black font-regular">
                                    {reviews} flashcards aguardando revisão hoje.
                                </p>
                            ) : (
                                <p className="text-p text-black font-regular">
                                    Nenhum flashcard precisa ser revisado hoje.
                                </p>
                            )}
                        </div>
                        
                        <div className="w-fit">
                            {reviews > 0 ? (
                                <Button href="/flashcards/revisao">
                                    Revisar agora
                                </Button>
                            ) : (
                                <Button variant="outline" href="/flashcards">
                                    Ver flashcards
                                </Button>
                            )}
                        </div>
                    </div>

                </div>
            </main>

        </div>
    )
}