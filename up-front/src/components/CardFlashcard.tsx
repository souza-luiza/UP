interface CardFlashcardProps {
    question: string;
    answer: string;
    level: number;
    onDelete?: () => void;
}

export default function CardFlashcard({ question, answer, level, onDelete }: CardFlashcardProps) {
    return (
        <div className="flex flex-col p-5 bg-white border border-secondary-dark/20 rounded-md-custom shadow-sm w-full md:w-80 min-h-[220px] justify-between gap-4">
            <div className="flex flex-col gap-3">
                <div>
                    <span className="text-xs font-semibold text-neutral-400 uppercase tracking-wider">Pergunta</span>
                    <p className="text-p font-medium text-black mt-0.5 whitespace-pre-wrap leading-snug">{question}</p>
                </div>
                
                <div className="border-t border-slate-100 pt-2">
                    <span className="text-xs font-semibold text-neutral-400 uppercase tracking-wider">Resposta</span>
                    <p className="text-p text-neutral-800 mt-0.5 whitespace-pre-wrap leading-snug">{answer}</p>
                </div>
            </div>

            <div className="flex items-center justify-between border-t border-slate-100 pt-3 mt-auto">
                <span className="text-sm font-semibold text-black">
                    Nível: <span className="text-neutral-500 font-medium">{level}</span>
                </span>
                
                {onDelete && (
                    <button 
                        onClick={onDelete}
                        className="text-xs font-semibold text-red-700 hover:text-red-800 hover:underline cursor-pointer outline-none"
                    >
                        Excluir
                    </button>
                )}
            </div>
        </div>
    );
}