interface CardInfosProps {
    variant: 'disciplinas' | 'disponibilidades' | 'sessões' | 'flashcards';
}

export default function CardInfos({ variant }: CardInfosProps) {

    const variantTexts = {
        disciplinas: 'disciplinas cadastradas',
        disponibilidades: 'disponibilidades cadastradas',
        sessões: 'sessões de estudo geradas',
        flashcards: 'flashcards cadastrados',
    }[variant];

    return(
        <div className="inline-flex px-5 py-[15px] flex-col justify-center items-start rounded-md-custom bg-secondary">
            <h2 className="text-h2 font-semibold text-black">3</h2>
            <p className="text-p font-regular text-black">
                {variantTexts}
            </p>
        </div>
    )

}