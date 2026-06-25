import Link from "next/link";

export interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
    variant?: 'primary' | 'secondary' | 'outline' | 'success' | 'danger';
    href?: string;
}

export default function Button({ children, className, variant = 'primary', href, ...props }: ButtonProps) {

    const variantStyles = {
        primary: 'bg-primary text-neutral-900 font-semibold border border-transparent hover:bg-primary/80 active:scale-[0.98]',
        secondary: 'bg-secondary text-neutral-900 font-semibold border border-transparent hover:bg-secondary/80 active:scale-[0.98]',
        outline: 'border border-neutral-900/30 text-neutral-900 font-semibold bg-transparent hover:bg-neutral-900/5 active:scale-[0.98]',
        success: 'bg-green-600 text-white font-semibold border border-transparent hover:bg-green-700 active:scale-[0.98]',
        danger: 'bg-red-700 text-white font-semibold border border-transparent hover:bg-red-800 active:scale-[0.98]'
    }[variant];

    const classes = `flex justify-center items-center cursor-pointer py-2 px-5 rounded-md-custom text-p font-semibold select-none outline-none focus-visible:ring-2 focus-visible:ring-primary transition-all duration-200 ease-in-out ${variantStyles} ${className || ''}`;

    if(href) {
        return (
            <Link href={href} className={classes}>
                {children}
            </Link>
        )
    }

    return(
        <button {...props} className={classes}>
            {children}
        </button>
    )
}