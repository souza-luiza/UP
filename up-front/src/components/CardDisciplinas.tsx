import Button from "./Button";

interface CardDisciplinasProps {
    name: string;
    difficulty: number;
    onDelete: () => void;
}

export default function CardDisciplinas({ name, difficulty, onDelete }: CardDisciplinasProps) {

    return(
        <div className="flex flex-col justify-between p-5 gap-4 rounded-md-custom bg-white border border-secondary-dark/20 text-secondary-dark w-full md:w-72 min-h-[160px]">
            <h4 className="text-h4 font-semibold text-black leading-snug">{name}</h4>
            <p className="text-p font-regular mt-1 opacity-80">
                Dificuldade: <span className="font-semibold text-black">{difficulty}/5</span>
            </p>
            <Button variant="danger" className="w-full py-1 text-sm" onClick={onDelete}>
                Excluir
            </Button>
        </div>
    )

}