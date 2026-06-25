import Button from "./Button";

interface CardDisciplinasProps {
    name: string;
    difficulty: number;
}

export default function CardDisciplinas({ name, difficulty }: CardDisciplinasProps) {

    return(
        <div className="inline-flex p-5 gap-2 flex-col justify-center items-start rounded-md-custom bg-secondary">
            <h4 className="text-h4 font-semibold text-black">{name}</h4>
            <p className="text-p font-regular text-black">
                Dificuldade: {difficulty}
            </p>
            <Button variant="outline">
                Excluir
            </Button>
        </div>
    )

}