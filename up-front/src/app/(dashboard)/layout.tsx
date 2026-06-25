"use client";

import { useState } from "react";
import { usePathname } from "next/navigation";
import Link from "next/link";
import Logo from "../components/Logo";

import CronogramaIcon from "../components/Icons/CronogramaIcon"; 
import FlashcardsIcon from "../components/Icons/FlashcardsIcon";
import TarefasIcon from "../components/Icons/TarefasIcon";

const navItems = [
    { href: "/cronograma",    label: "Cronograma",  Icon: CronogramaIcon },
    { href: "/flashcards",    label: "Flashcards",  Icon: FlashcardsIcon },
    { href: "/tarefas",       label: "Tarefas",     Icon: TarefasIcon    },
];

export default function DashboardLayout({ children }: { children: React.ReactNode }) {
    const [aberta, setAberta] = useState(false);
    const pathname = usePathname();

    return (
        <div className="flex min-h-screen" style={{ backgroundColor: "#f5eef0" }}>

            Formato padrão para as outras páginas

            Aqui vai ter a parte da sidebar e searchbar para a pesquisa + espaço em branco
        </div>
    );
}