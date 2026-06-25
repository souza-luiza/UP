interface CardInfosProps {
    variant: 'disciplinas' | 'disponibilidades' | 'sessões' | 'flashcards';
    value: number | string;
}

export default function CardInfos({ variant, value }: CardInfosProps) {

    const variantTexts = {
        disciplinas: 'disciplinas cadastradas',
        disponibilidades: 'disponibilidades cadastradas',
        sessões: 'sessões de estudo geradas',
        flashcards: 'flashcards cadastrados',
    }[variant];

    return(
        <div className="inline-flex flex-col justify-center items-start px-5 py-4 rounded-md-custom transition-all bg-white border border-secondary-dark/20 text-secondary-dark">
            <h2 className="text-h2 font-semibold text-black leading-tight">{value}</h2>
            <p className="text-p font-regular mt-1 opacity-90">
                {variantTexts}
            </p>
        </div>
    )

}