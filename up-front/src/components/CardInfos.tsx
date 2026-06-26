import Link from "next/link";

interface CardInfosProps {
    variant: 'disciplinas' | 'disponibilidades' | 'flashcards';
    value: number | string;
}

export default function CardInfos({ variant, value }: CardInfosProps) {

    const variantTexts = {
        disciplinas: 'disciplinas cadastradas',
        disponibilidades: 'disponibilidades cadastradas',
        flashcards: 'flashcards cadastrados',
    }[variant];

    const variantUrls = {
        disciplinas: '/disciplinas',
        disponibilidades: '/disponibilidades',
        flashcards: '/flashcards',
    }[variant];

    return(
        <Link href={variantUrls} 
            className="flex flex-col justify-center items-start px-5 py-4 rounded-md-custom bg-white border border-secondary-dark/20 text-secondary-dark w-full md:w-80 cursor-pointer select-none transition-all duration-200 hover:border-primary/50 hover:shadow-sm outline-none focus-visible:ring-2 focus-visible:ring-primary"
        >
            <h2 className="text-h2 font-semibold text-black leading-tight">{value}</h2>
            <p className="text-p font-regular mt-1 opacity-90">
                {variantTexts}
            </p>
        </Link>
    )

}