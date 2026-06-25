"use client";

import { useState } from "react";
import UserIcon from "../components/Icons/UserIcon";
import AtSign from "../components/Icons/AtSignIcon";
import Key from "../components/Icons/KeyIcon";
import EyeClosed from "../components/Icons/EyeClosedIcon";
import EyeOpen from "../components/Icons/EyeOpenIcon";
import Button from "@/components/Button";
import Logo from "@/components/Logo";

export default function RegisterPage() {
    const [nome, setNome] = useState("");
    const [email, setEmail] = useState("");
    const [senha, setSenha] = useState("");
    const [confirmarSenha, setConfirmarSenha] = useState("");
    const [mostrarSenha, setMostrarSenha] = useState(false);
    const [mostrarConfirmar, setMostrarConfirmar] = useState(false);
    
    const [erro, setErro] = useState("");

    function handleCadastrar(e: React.SubmitEvent<HTMLFormElement>) {
        e.preventDefault();
        
        setErro("");

        if (senha !== confirmarSenha) {
            setErro("As senhas não coincidem.");
            return;
        }
        
        // CHAMA PARA API AQUI
        console.log({ nome, email, senha });
    }

    const inputClass = "w-full px-3 py-2 rounded-md bg-white/30 border border-black/20 text-p text-black placeholder:text-black/50 focus:outline-none focus:ring-1 focus:ring-black/40";

    return (
        <div className="flex min-h-screen flex-col justify-between">
            <header className="flex w-full bg-primary py-1.5 px-1 md:px-20 justify-between items-center">
                <Logo primaryColor={false} />
                <Button variant="outline" href="/">Voltar</Button>
            </header>

            <main className="flex flex-1 items-center justify-center">
                <div className="bg-primary rounded-lg p-8 w-full max-w-sm">
                    <h1 className="text-h2 font-semibold text-center text-black mb-1">Cadastro</h1>
                    <p className="text-p font-regular text-black text-center mb-4">
                        Crie sua conta para acessar a plataforma
                    </p>

                    <form onSubmit={handleCadastrar} className="flex flex-col gap-3">

                        <div className="flex flex-col gap-1">
                            <label className="text-p font-regular text-black flex items-center gap-1">
                                <UserIcon primaryColor={false} />
                                Nome
                            </label>
                            <input
                                type="text"
                                placeholder="Escreva aqui o seu nome"
                                value={nome}
                                onChange={(e) => setNome(e.target.value)}
                                required
                                className={inputClass}
                            />
                        </div>

                        <div className="flex flex-col gap-1">
                            <label className="text-p font-regular text-black flex items-center gap-1">
                                <AtSign primaryColor={false} />
                                E-mail
                            </label>
                            <input
                                type="email"
                                placeholder="Escreva aqui o seu e-mail"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                                className={inputClass}
                            />
                        </div>

                        <div className="flex flex-col gap-1">
                            <label className="text-p font-regular text-black flex items-center gap-1">
                                <Key primaryColor={false} />
                                Senha
                            </label>
                            <div className="relative">
                                <input
                                    type={mostrarSenha ? "text" : "password"}
                                    placeholder="Escreva aqui a sua senha"
                                    value={senha}
                                    onChange={(e) => setSenha(e.target.value)}
                                    required
                                    className={`${inputClass} pr-10`}
                                />
                                <button
                                    type="button"
                                    onClick={() => setMostrarSenha(!mostrarSenha)}
                                    className="absolute right-3 top-1/2 -translate-y-1/2 text-black/60 hover:text-secondary-dark/80 transition"
                                >
                                    {mostrarSenha ? <EyeClosed primaryColor={false} /> : <EyeOpen primaryColor={false} />}
                                </button>
                            </div>
                        </div>

                        <div className="flex flex-col gap-1">
                            <label className="text-p font-regular text-black flex items-center gap-1">
                                <Key primaryColor={false} />
                                Confirme a sua Senha
                            </label>
                            <div className="relative">
                                <input
                                    type={mostrarConfirmar ? "text" : "password"}
                                    placeholder="Confirme a sua senha"
                                    value={confirmarSenha}
                                    onChange={(e) => setConfirmarSenha(e.target.value)}
                                    required
                                    className={`${inputClass} pr-10 ${erro ? 'border-red-500/50 focus:ring-red-500/40' : ''}`}
                                />
                                <button
                                    type="button"
                                    onClick={() => setMostrarConfirmar(!mostrarConfirmar)}
                                    className="absolute right-3 top-1/2 -translate-y-1/2 text-black/60 hover:text-secondary-dark/80 transition"
                                >
                                    {mostrarConfirmar ? <EyeClosed primaryColor={false} /> : <EyeOpen primaryColor={false} />}
                                </button>
                            </div>
                            
                            {erro && (
                                <span className="text-xs font-semibold text-red-600 mt-0.5 animate-fadeIn">
                                    {erro}
                                </span>
                            )}
                        </div>

                        <div className="flex justify-end">
                            <a href="/login" className="text-sm font-regular text-black/70 hover:underline">
                                Já possui cadastro?
                            </a>
                        </div>

                        <div className="flex gap-3 mt-1 w-full">
                            <Button variant="outline" href="/" className="w-1/3">
                                Voltar
                            </Button>
                            <Button type="submit" variant="secondary" className="w-2/3">
                                Cadastrar
                            </Button>
                        </div>

                    </form>
                </div>
            </main>

            <footer className="bg-primary w-full text-center p-2">
                <p className="text-p text-black font-light">© 2026 UP. Todos os direitos reservados.</p>
            </footer>

        </div>
    );
}