import Button from "./Button";

interface CardDisponibilidadeProps {
    dayOfWeek: string;
    startTime: string;
    endTime: string;
    onDelete?: () => void;
}

export default function CardDisponibilidade({ dayOfWeek, startTime, endTime, onDelete }: CardDisponibilidadeProps) {

    const formattedStart = startTime?.slice(0, 5) || startTime;
    const formattedEnd = endTime?.slice(0, 5) || endTime;

    return(
        <div className="flex flex-col justify-between p-5 gap-4 rounded-md-custom bg-white border border-secondary-dark/20 text-secondary-dark w-full md:w-72 min-h-[160px]">
            <h4 className="text-h4 font-semibold text-black leading-snug">{dayOfWeek}</h4>
            <p className="text-p font-regular mt-1 opacity-85">
                Horário: <span className="font-semibold text-black">{formattedStart} às {formattedEnd}</span>
            </p>

            {onDelete && (
                <Button onClick={onDelete} variant="danger" className="w-full py-1 text-sm">
                    Excluir
                </Button>
            )}
        </div>
    )

}