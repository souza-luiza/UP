"use client";

import { login } from "@/services/api";
import { useRouter } from "next/navigation";
import { useState } from "react";
import AtSign from "../components/Icons/AtSignIcon";
import Key from "../components/Icons/KeyIcon";
import EyeClosed from "../components/Icons/EyeClosedIcon";
import EyeOpen from "../components/Icons/EyeOpenIcon";
import Button from "@/components/Button";
import Logo from "@/components/Logo";
import { toast } from "react-toastify";

export default function LoginPage() {
    const [email, setEmail] = useState("");
    const [senha, setSenha] = useState("");
    const [mostrarSenha, setMostrarSenha] = useState(false);
    const router = useRouter();

    async function handleEntrar(e: React.SubmitEvent<HTMLFormElement>) {
        e.preventDefault();
        
        try {
            const data = await login(
                email,
                senha
            );

            localStorage.setItem(
                "accessToken",
                data.accessToken
            );

            router.push("/perfil");

        } catch {

            toast.error("E-mail ou senha inválidos.");

        }
    }

    const inputClass = "w-full px-3 py-2 rounded-md bg-white/30 border border-black/20 text-p text-black placeholder:text-black/50 focus:outline-none focus:ring-1 focus:ring-black/40";

    return (
        <div className="flex min-h-screen flex-col justify-between">

            <header className="flex w-full bg-primary py-1.5 px-1 md:px-20 justify-between items-center">
                <Logo primaryColor={false} />
                <Button variant="outline" href="/">
                    Voltar
                </Button>
            </header>

            <main className="flex flex-1 items-center justify-center">
                <div className="bg-primary rounded-lg p-8 w-full max-w-sm">

                    <h1 className="text-h2 font-semibold text-center text-black mb-1">Login</h1>
                    <p className="text-p font-regular text-black text-center mb-4">
                        Faça login para acessar sua conta
                    </p>

                    <form onSubmit={handleEntrar} className="flex flex-col gap-3">

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
                                    aria-label={mostrarSenha ? "Esconder senha" : "Mostrar senha"}
                                >
                                    {mostrarSenha ? <EyeClosed primaryColor={false} /> : <EyeOpen primaryColor={false} />}
                                </button>
                            </div>
                        </div>

                        <div className="flex justify-between">
                            <a href="/recuperar-senha" className="text-sm font-regular text-black/70 hover:underline cursor-pointer">
                                Esqueceu a senha?
                            </a>
                            <a href="/register" className="text-sm font-regular text-black/70 hover:underline">
                                Não tem cadastro?
                            </a>
                        </div>

                        <div className="flex gap-3 mt-1 w-full">
                            <Button variant="outline" href="/" className="w-1/3">
                                Voltar
                            </Button>
                            <Button type="submit" variant="secondary" className="w-2/3">
                                Entrar
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