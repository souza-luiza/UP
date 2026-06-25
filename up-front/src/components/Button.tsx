import Link from "next/link";

export interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
    variant?: 'primary' | 'secondary' | 'outline';
    href?: string;
}

export default function Button({ children, className, variant = 'primary', href, ...props }: ButtonProps) {

    const variantStyles = {
        primary: 'bg-primary text-black border-2 border-transparent hover:shadow-[0_0_8px_#B39EB5]',
        secondary: 'bg-secondary text-black border-2 border-transparent hover:shadow-[0_0_8px_#ADEBB3]',
        outline: 'border-2 border-black text-black hover:shadow-[0_0_4px_#000000]'
    }[variant];

    const classes = `flex justify-center items-center py-1.5 px-5 rounded-md-custom text-p font-regular transition-all duration-150 ease-in-out ${variantStyles} ${className || ''}`;

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